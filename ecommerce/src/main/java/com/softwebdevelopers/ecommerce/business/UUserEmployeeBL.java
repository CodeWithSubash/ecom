package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.listeners.thankyou.OnMailSendingEvent;
import com.softwebdevelopers.ecommerce.models.custom.IProductCount;
import com.softwebdevelopers.ecommerce.models.custom.IRetailerCount;
import com.softwebdevelopers.ecommerce.models.custom.ProductCount;
import com.softwebdevelopers.ecommerce.models.custom.RetailerCount;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserEmployeeMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserEmployeeRecordMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserMapper;
import com.softwebdevelopers.ecommerce.models.role.Role;
import com.softwebdevelopers.ecommerce.models.user.UserEmployeeRecord;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.models.user.UserEmployee;
import com.softwebdevelopers.ecommerce.repository.OOrderRepository;
import com.softwebdevelopers.ecommerce.repository.RRoleRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.impl.UUserEmployeeServiceImpl;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UUserEmployeeBL {

    @Value("${email.app-url}")
    private String APP_URL;

    @Autowired
    private UUserEmployeeServiceImpl service;

    @Autowired
    UUserRepository repo;

    @Autowired
    RRoleRepository roleRepository;

    @Autowired
    OOrderRepository orderRepository;

    @Autowired
    private UserEmployeeMapper mapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserEmployeeRecordMapper recordMapper;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private AuditActivityRecordBL auditService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public PaginationRecordsDto<UUserEmployeeDto> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<UUserEmployeeDto> dto = mapper.toDto(service.getAll(page));
        return dto;
    }

    public UUserEmployeeDto getById(Long id) throws RecordNotFoundException {
        UUserEmployeeDto dto = mapper.toDto(service.getById(id));

        return dto;
    }

    public UUserEmployeeDto create(UUserEmployeeDto userInfo) {
        String password = ECOMUtilities.generatePassword();
        User user = new User().toBuilder()
                .name(Preconditions.checkNotNull(userInfo.getFirstname()) ? userInfo.getFirstname().concat(" ").concat(userInfo.getLastname()) : null)
                .username(userInfo.getUsername())
                .email(userInfo.getEmail())
                .password(encoder.encode(password))
                .enabled(true)
                .isEmailVerified(true)
                .build();

        user.setRoles(Arrays.asList(getRole(userInfo.getRole())));

        UserEmployee entity = mapper.toEntity(userInfo);
        entity.setUser(user);
        user.setUserEmployee(entity);
        User created = repo.save(user);
        if (created == null) {
            log.warn("The user [{}] creation failed", user.getUsername());
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(UUserBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Added new <b>User</b>, <b>"
                                .concat(Preconditions.checkNotNull(created.getName()) ? created.getName() : created.getUsername())
                                .concat("</b>"))
                        .build());

        ECOMSecurityContextHolder securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = repo.findByUsername(securityContext.getUsername());
        // Email Publisher
        EmailDto emailDto = new EmailDto().toBuilder()
                .subject("Thank you for registering with us.")
                .message("Thank you for registering with us.")
                .password("Your username is: " + user.getUsername() + " and password is: " + password)
                .recipientEmails(Arrays.asList(user.getEmail(), userCtx.get().getEmail()))
                .build();
        applicationEventPublisher.publishEvent(
                new OnMailSendingEvent(emailDto, APP_URL));

        return mapper.toDto(created.getUserEmployee());
    }

    public UUserEmployeeDto update(UUserEmployeeDto userInfo) {

        UserEmployee updated = service.update(mapper.toEntity(userInfo), userInfo.getRole());
        if (updated == null) {
            log.warn("The user [{}] creation failed", userInfo.getUsername());
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(UUserBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Updated User, <b>"
                                .concat(Preconditions.checkNotNull(updated.getFirstname()) ? updated.getFirstname() : updated.getUsername())
                                .concat("</b>"))
                        .build());

        return mapper.toDto(updated);
    }

    public UUserEmployeeRecordDto getEmployeeRecord() {
        UUserEmployeeRecordDto dto = recordMapper.toDto(service.getEmployeeRecord());

        List<IProductCount> counts = orderRepository.countEmployeeTotalOrder(dto.getUserId());
        dto.setMonthlyGraph(counts.stream().map(item -> new ProductCount().toBuilder()
                .productId(item.getProductId())
                .oYear(item.getOYear())
                .oMonth(item.getOMonth())
                .monthName(item.getMonthName())
                .productName(item.getProductName())
                .productCount(item.getProductCount())
                .build()).collect(Collectors.toList()));
        return dto;
    }

    private Role
    getRole(String role) {
        switch (role) {
            case "GUEST":
                return roleRepository.findByName(ERole.ROLE_GUEST.name())
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            case "RETAILER":
                return roleRepository.findByName(ERole.ROLE_RETAILER.name())
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            case "SALES":
                return roleRepository.findByName(ERole.ROLE_SALES.name())
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            case "ADMIN":
                return roleRepository.findByName(ERole.ROLE_ADMIN.name())
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            case "SUPERADMIN":
                return roleRepository.findByName(ERole.ROLE_SUPERADMIN.name())
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            default:
                return roleRepository.findByName(ERole.ROLE_GUEST.name())
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        }
    }
}
