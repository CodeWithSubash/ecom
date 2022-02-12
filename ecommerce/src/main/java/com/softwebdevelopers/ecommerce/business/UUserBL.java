package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.listeners.thankyou.OnMailSendingEvent;
import com.softwebdevelopers.ecommerce.exceptions.BadRequestException;
import com.softwebdevelopers.ecommerce.exceptions.RecordConflictException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.enums.EUserType;
import com.softwebdevelopers.ecommerce.models.modelmapper.SelectItemsMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserEmployeeMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserRetailerMapper;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserMapper;
import com.softwebdevelopers.ecommerce.models.user.UserEmployee;
import com.softwebdevelopers.ecommerce.models.user.UserRetailer;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IUUserService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UUserBL {

    @Value("${email.app-url}")
    private String APP_URL;

    @Autowired
    private IUUserService service;

    @Autowired
    UUserRepository repo;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserEmployeeMapper infoMapper;

    @Autowired
    private UserRetailerMapper kycMapper;

    @Autowired
    SelectItemsMapper itemsMapper;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private AuditActivityRecordBL auditService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public UUserDto getUser() {
        User entity = service.getUser();

        UUserDto userDto = mapper.toDto(entity);
//        userDto.setAssignedRetailers(Preconditions.checkNotNull(entity.getRetailers()) ?
//                entity.getRetailers().stream().map(item ->
//                    Preconditions.checkNotNull(item.getUserRetailer()) ? kycMapper.toDto(item.getUserRetailer()) : null
//                ).collect(Collectors.toList()) :
//                null);

        log.info("The user with user Id: [{}] returned successfully.", entity.getId());
        return userDto;
    }

    public PaginationRecordsDto<UUserDto> getAllUser(PagingSortingAndSearchDto page, String userType) {
        return mapper.toDto(service.getAll(page, userType));
    }

    public List<SelectItemsDto.SelectItemIntDto> getAllList(String type) throws RecordNotFoundException {
        List<SelectItemsDto.SelectItemIntDto> dtoList = new ArrayList<>();
        List<User> userList = service.findAllByUserIdAndType(type);
        for (User user : userList) {
            dtoList.add(itemsMapper.toDto(user.getId(), user.getName()));
        }
        return dtoList;
    }

    public List<SelectItemsDto.SelectItemIntDto> getAllSalesRepresentativeList(String type) throws RecordNotFoundException {
        List<SelectItemsDto.SelectItemIntDto> dtoList = new ArrayList<>();
        List<User> userList = service.findAllByUserIdAndType(type);
        for (User user : userList) {
            if (user.getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                dtoList.add(itemsMapper.toDto(user.getId(), user.getName()));
            }
        }
        return dtoList;
    }

    public List<SelectItemsDto.SelectItemIntDto> getAllWholesalerList(String type) throws RecordNotFoundException {
        List<SelectItemsDto.SelectItemIntDto> dtoList = new ArrayList<>();
        List<User> userList = service.findAllByUserIdAndType(type);
        for (User user : userList) {
            if (user.getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                dtoList.add(itemsMapper.toDto(user.getId(), user.getName()));
            }
        }
        return dtoList;
    }

    public UUserDto changePassword(URegistrationDto dto) {

        User entity = service.getUser();
        if (Preconditions.checkNotNull(entity)) {

            if (!entity.isEnabled()) {
                throw new BadRequestException("User can't change the password as the user is not verified.");
            }

            if (!encoder.matches(dto.getOldPassword(), entity.getPassword())) {
                throw new BadRequestException("The provided old password doesn't match.");
            }

            entity.setPassword(dto.getPassword());
            entity = service.changePassword(entity);

            log.info("The user with user Id: [{}] update successfully. The password changed successfully.", entity.getId());
            return mapper.toDto(entity);
        } else {

            log.error("The user must be logged in to change the password.");
            throw new RecordConflictException("The user must be logged in to change the password.");
        }
    }

    public UUserDto getById(Long id) throws RecordNotFoundException {
        User entity = service.getById(id);
        return mapper.toDto(entity);
    }

    public UUserDto create(UUserDto user) {
//        String password = ECOMUtilities.generatePassword();
        String password = UUID.randomUUID().toString().split("-")[0];
        String name = user.getUserType().equalsIgnoreCase(EUserType.EMPLOYEE.name()) && Preconditions.checkNotNull(user.getUserEmployee()) && Preconditions.checkNotBlank(user.getUserEmployee().getFirstname()) ?
                Preconditions.checkNotBlank(user.getUserEmployee().getLastname()) ?
                        user.getUsername().concat(" ").concat(user.getUserEmployee().getLastname()) :
                        user.getUserEmployee().getFirstname() :
                user.getUserType().equalsIgnoreCase(EUserType.RETAILER.name()) && Preconditions.checkNotNull(user.getUserRetailer()) && Preconditions.checkNotBlank(user.getUserRetailer().getBusinessName()) ?
                        user.getUserRetailer().getBusinessName() :
                        null;

        User entity = new User().toBuilder()
                .id(user.getId())
                .name(name)
                .username(user.getUsername())
                .email(user.getEmail())
                .password(password)
                .enabled(true)
                .isEmailVerified(true)
                .userType(user.getUserType())
                .build();

        if (user.getUserType().equalsIgnoreCase(EUserType.EMPLOYEE.name()) && Preconditions.checkNotNull(user.getUserEmployee())) {
            UserEmployee userInfo = infoMapper.toEntity(user.getUserEmployee());
            userInfo.setUser(entity);
            userInfo.setUsername(user.getUsername());
            userInfo.setEmail(user.getEmail());
            entity.setUserEmployee(userInfo);
        } else if (user.getUserType().equalsIgnoreCase(EUserType.RETAILER.name()) && Preconditions.checkNotNull(user.getUserRetailer())) {
            UserRetailer userKyc = kycMapper.toEntity(user.getUserRetailer());
            userKyc.setUser(entity);
            userKyc.setEmail(user.getEmail());
            entity.setUserRetailer(userKyc);
        } else {
            return null;
        }

        User created = service.create(entity, user.getRole());
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

        return mapper.toDto(created);
    }

    public UUserDto updateById(UUserDto user, Long id) throws RecordNotFoundException {

        String name = user.getUserType().equalsIgnoreCase(EUserType.EMPLOYEE.name()) && Preconditions.checkNotNull(user.getUserEmployee()) && Preconditions.checkNotBlank(user.getUserEmployee().getFirstname()) ?
                Preconditions.checkNotBlank(user.getUserEmployee().getLastname()) ?
                        user.getUsername().concat(" ").concat(user.getUserEmployee().getLastname()) :
                        user.getUserEmployee().getFirstname() :
                user.getUserType().equalsIgnoreCase(EUserType.RETAILER.name()) && Preconditions.checkNotNull(user.getUserRetailer()) && Preconditions.checkNotBlank(user.getUserRetailer().getBusinessName()) ?
                        user.getUserRetailer().getBusinessName() :
                        null;

        User entity = new User().toBuilder()
                .id(id)
                .name(name)
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        if (user.getUserType().equalsIgnoreCase(EUserType.EMPLOYEE.name()) && Preconditions.checkNotNull(user.getUserEmployee())) {
            UserEmployee userInfo = infoMapper.toEntity(user.getUserEmployee());
            userInfo.setUser(entity);
            userInfo.setUsername(user.getUsername());
            userInfo.setEmail(user.getEmail());
            entity.setUserEmployee(userInfo);
        } else if (user.getUserType().equalsIgnoreCase(EUserType.RETAILER.name()) && Preconditions.checkNotNull(user.getUserRetailer())) {
            UserRetailer userKyc = kycMapper.toEntity(user.getUserRetailer());
            userKyc.setUser(entity);
            userKyc.setEmail(user.getEmail());
            entity.setUserRetailer(userKyc);
        } else {
            return null;
        }

        User updated = service.update(entity, user.getRole());
        if (updated == null) {
            log.warn("The user id: [{}] update failed", id);
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(UUserBL.class.toString())
                        .method(ECOMConstants.UPDATED)
                        .asString("Updated User, <b>"
                                .concat(Preconditions.checkNotNull(updated.getName()) ? updated.getName() : updated.getUsername())
                                .concat("</b>"))
                        .build());

        return mapper.toDto(updated);
    }

    public void deleteById(Long id) throws RecordNotFoundException {
        User deleted = service.deleteSoft(id);
        if (Preconditions.checkNotNull(deleted)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(UUserBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Deleted User, <b>"
                                    .concat(Preconditions.checkNotNull(deleted.getName()) ? deleted.getName() : deleted.getUsername())
                                    .concat("</b>"))
                            .build());
        }
    }

    public UUserDto assignEmployeeRetailers(Long id, List<Long> retailerIds) {

        User updated = service.assignEmployeeRetailer(id, retailerIds);
        if (updated == null) {
            log.warn("The user [{}] creation failed", id);
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(UUserBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Updated User, <b>"
//                                .concat(Preconditions.checkNotNull(updated.getFirstname()) ? updated.getFirstname() : updated.getUsername())
                                .concat("</b>"))
                        .build());

        return mapper.toDto(updated);
    }
}
