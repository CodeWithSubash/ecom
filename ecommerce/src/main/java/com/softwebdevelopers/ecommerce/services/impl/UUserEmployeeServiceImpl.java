package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordConflictException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.role.Role;
import com.softwebdevelopers.ecommerce.models.user.UserEmployeeRecord;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.models.user.UserEmployee;
import com.softwebdevelopers.ecommerce.repository.*;
import com.softwebdevelopers.ecommerce.services.IUUserEmployeeService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UUserEmployeeServiceImpl implements IUUserEmployeeService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    UUserEmployeesRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Autowired
    RRoleRepository roleRepository;

    @Autowired
    UUserEmployeeRecordRepository recordRepository;

    @Override
    public Page<UserEmployee> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<UserEmployee> infoList = null;
        if (Preconditions.checkNotBlank(page.getKeyword())) {
            infoList = repo.findByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            infoList = repo.findAll(indexPageWithElements);
        }

        if (Preconditions.checkNotNull(infoList)) {
            log.info("The user information list returned successfully.");
            return infoList;
        } else {
            log.warn("The user information [0] fetched failed. The product list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public UserEmployee getById(Long id) throws RecordNotFoundException {
        Optional<UserEmployee> info = repo.findById(id);

        if (info.isPresent()) {
            log.info("The user information with Id: [{}] returned successfully.", id);
            return info.get();
        } else {
            log.warn("The user information [0] fetched failed. The provided Id: [{}] is not found.", id);
            throw new RecordNotFoundException("No product record exists for given Id: [" + id + "].");
        }
    }

    @Override
    public UserEmployeeRecord getEmployeeRecord() throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = userRepository.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            Optional<UserEmployeeRecord> info = recordRepository.findByUserId(userCtx.get().getId());

            if (info.isPresent()) {
                log.info("The user information with Id: [{}] returned successfully.", userCtx.get().getId());
                return info.get();
            } else {
                log.warn("The user information [0] fetched failed. The provided Id: [{}] is not found.", userCtx.get().getId());
                throw new RecordNotFoundException("No product record exists for given Id: [" + userCtx.get().getId() + "].");
            }
        } else {
                log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                        securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

//    @Override
//    public Object getEmployeeRecord() throws RecordNotFoundException {
//        securityContext = new ECOMSecurityContextHolder();
//        Optional<User> userCtx = userRepository.findByUsername(securityContext.getUsername());
//        try {
//            if (userCtx.isPresent()) {
//                Optional list = em.createNamedStoredProcedureQuery("getEmployeeRecord").setParameter("userId", userCtx.get().getId()).getResultList().stream().findFirst();
//                Object info = repo.getEmployeeRecord(userCtx.get().getId());
//
//                if (Preconditions.checkNotNull(info)) {
//                    log.info("The user information with Id: [{}] returned successfully.", userCtx.get().getId());
//                    return info;
//                } else {
//                    log.warn("The user information [0] fetched failed. The provided Id: [{}] is not found.", userCtx.get().getId());
//                    throw new RecordNotFoundException("No product record exists for given Id: [" + userCtx.get().getId() + "].");
//                }
//            } else {
//                log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
//                        securityContext.getUsername());
//                throw new RecordNotFoundException(
//                        "No user record exists for given username: [" + securityContext.getUsername() + "].");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public UserEmployee create(UserEmployee userInfo) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = userRepository.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            try {
                UserEmployee entity = repo.save(userInfo);
                if (entity != null) {
                    log.info("The user information [{}] created successfully.", entity.getId());
                } else {
                    log.info("The user information [{}] creation failed.", userInfo.getUsername());
                }
                return entity;
            } catch (Exception e) {
                log.error("The provided username/email: [{}] already exists.", userInfo.getUsername());
                throw new RecordConflictException("The provided username: [" + userInfo.getUsername() +
                        "] or email: [" + userInfo.getEmail() + "] already exists.");
            }

        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public UserEmployee update(UserEmployee userInfo, String role) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = userRepository.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            Optional<UserEmployee> entity = repo.findById(userInfo.getId());
            if (entity.isPresent()) {
                try {
                    User user = entity.get().getUser();
                    userInfo.setUser(user);
                    UserEmployee newEntity = repo.save(userInfo);

                    if (newEntity != null) {
                        log.info("The user info [{}] updated successfully.", newEntity.getId());
                    } else {
                        log.info("The user info [{}] update failed.", entity.get().getUsername());
                    }
                    return newEntity;
                } catch (Exception e) {
                    log.error("The provided username/email: [{}] already exists.", userInfo.getUsername());
                    throw new RecordConflictException("The provided username: [" + userInfo.getUsername() +
                            "] or email: [" + userInfo.getEmail() + "] already exists.");
                }
            } else {
                log.warn("The user information [0] update failed. The provided Id: [{}] is not found.", userInfo.getId());
                throw new RecordNotFoundException("No user information record exists for given Id: [" + userInfo.getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public UserEmployee deleteSoft(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<UserEmployee> entity = repo.findById(id);

            if (entity.isPresent()) {
                UserEmployee newEntity = entity.get();
                newEntity.setDeletedDate(LocalDateTime.now());
                newEntity = repo.save(newEntity);

                if (newEntity != null) {
                    log.info("The user information [{}] soft delete successfully.", newEntity.getId());
                } else {
                    log.info("The user information [{}] soft delete failed.", id);
                }
                return newEntity;
            } else {
                log.warn("The user [0] update failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No user record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    private Role getRole(String role) {
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
