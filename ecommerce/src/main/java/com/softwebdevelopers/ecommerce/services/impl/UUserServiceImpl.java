package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordConflictException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.user.UserVerificationToken;
import com.softwebdevelopers.ecommerce.models.user.UResetPasswordToken;
import com.softwebdevelopers.ecommerce.repository.UResetPasswordTokensRepository;
import com.softwebdevelopers.ecommerce.repository.UVerificationTokenRepository;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.role.Role;
import com.softwebdevelopers.ecommerce.repository.RRoleRepository;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.services.IUUserService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UUserServiceImpl implements IUUserService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    UUserRepository repo;

    @Autowired
    RRoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UVerificationTokenRepository tokenRepository;

    @Autowired
    private UResetPasswordTokensRepository passwordTokensRepository;

    @Override
    public User registerUserAccount(User user, Collection<String> strRoles) {
        user.setPassword(encoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_RETAILER.name())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "GUEST":
                        Role guestRole = roleRepository.findByName(ERole.ROLE_GUEST.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(guestRole);
                        break;
                    case "RETAILER":
                        Role retailerRole = roleRepository.findByName(ERole.ROLE_RETAILER.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(retailerRole);
                        break;
                    case "SALES":
                        Role salesRole = roleRepository.findByName(ERole.ROLE_SALES.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(salesRole);
                        break;

                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "SUPERADMIN":
                        Role superadminRole = roleRepository.findByName(ERole.ROLE_SUPERADMIN.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(superadminRole);
                        break;
                    default:
                        Role defaultRole = roleRepository.findByName(ERole.ROLE_GUEST.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(defaultRole);
                }
            });
        }

        user.setRoles(roles);
        User registeredUser = repo.save(user);

        return registeredUser;
    }

    @Override
    public UserVerificationToken createVerificationToken(UserVerificationToken verificationToken) {
        return tokenRepository.save(verificationToken);
    }

    @Override
    public UserVerificationToken findByVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public User updateRegisteredUser(User user) {
        return repo.save(user);
    }

    @Override
    public void updateVerificationTokenEnableFlag(Long id, Long userId) {

        try {
            tokenRepository.updateExpiryFlag(id, userId);

            log.info("The User Token Verification expired flag update successfully.");
        } catch (Exception e) {
            log.warn(
                    "The User Token Verification expired flag update failed. The provided user id: [{}] is not found.",
                    userId);
        }
    }

    @Override
    public User changePassword(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public UResetPasswordToken createPasswordToken(UResetPasswordToken verificationToken) {
        UResetPasswordToken passwordInfo = passwordTokensRepository.save(verificationToken);

        if (Preconditions.checkNotNull(passwordInfo)) {
            log.info("The forgot password token: [{}] generated successfully.",
                    passwordInfo.getId());
            return passwordInfo;
        } else {
            log.info("The forgot password token generation failed.");
            throw new RecordNotFoundException("The forgot password token generation failed.");
        }
    }

    @Override
    public UResetPasswordToken findByPasswordToken(String token) {
        return passwordTokensRepository.findByToken(token);
    }

    @Override
    public void updatePasswordEnableFlag(Long id, Long userId) {

        try {
            passwordTokensRepository.updateExpiryFlag(id, userId);

            log.info("The User Password expired flag update successfully.");
        } catch (Exception e) {
            log.warn(
                    "The User Password expired flag update failed. The provided advisor id: [{}] is not found.",
                    userId);
        }
    }

    @Override
    public User getUserByEmail(String email) {

        Optional<User> user = repo.findByEmail(email);

        if (user.isPresent()) {

            log.info("The user with Id: [{}] fetch success.", user.get().getId());
            return user.get();
        } else {
            log.warn("The user [0] fetch failed. The provided email: [{}] is not found.", email);
            throw new RecordNotFoundException("No user record exists for given user email: [" + email + "].");
        }
    }

    @Override
    public User getUser() throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = repo.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            User entity = user.get();

            log.info("The user with Id: [{}] returned successfully.", entity.getId());
            return entity;
        } else {
            log.warn("The user [0] fetch failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<User> getAll(PagingSortingAndSearchDto page, String userType) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = repo.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<User> userList = null;
            if (Preconditions.checkNotBlank(page.getKeyword())) {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                    userList = repo.findByOwnerAndKeywordWithPagination(user.get().getId(), userType, page.getKeyword(), indexPageWithElements);
                } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))) {
                    userList = repo.findByKeywordWithPagination(userType, page.getKeyword(), indexPageWithElements);
                }
            } else {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                    userList = repo.findAllByOwnerAndType(user.get().getId(), userType, indexPageWithElements);
                } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                        || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))) {
                    userList = repo.findAllByType(userType, indexPageWithElements);
                }
            }

            if (userList != null && !userList.isEmpty()) {
                log.info("The users returned successfully.");
                return userList;
            } else {
                log.warn(
                        "The users fetched failed. The users are not found.");
                throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

//    @Override
//    public List<User> getAllByType(String type) throws RecordNotFoundException {
//        securityContext = new ECOMSecurityContextHolder();
//        Optional<User> user = repo.findByUsername(securityContext.getUsername());
//
//        if (user.isPresent()) {
//            List<User> userList = repo.findAllByType(type);
//            if (userList != null && !userList.isEmpty()) {
//                log.info("The users for user type: [{}] returned successfully.", type);
//                return userList;
//            } else {
//                log.warn(
//                        "The user [0] fetched failed. The user for provided user with Type: [{}] is not found.",
//                        type);
//                throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
//            }
//        } else {
//            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
//                    securityContext.getUsername());
//            throw new RecordNotFoundException(
//                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
//        }
//    }

    @Override
    public List<User> findAllByUserIdAndType(String type) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = repo.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            List<User> userList = repo.findAllByOwnerIdOrIsNullAndType(user.get().getId(), type);
            if (userList != null && !userList.isEmpty()) {
                log.info("The users for user type: [{}] returned successfully.", type);
                return userList;
            } else {
                log.warn(
                        "The user [0] fetched failed. The user for provided user with Type: [{}] is not found.",
                        type);
                throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public User getById(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = repo.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<User> entity = repo.findById(id);

            if (entity.isPresent()) {
                log.info("The user with Id: [{}] returned successfully.", id);
                return entity.get();
            } else {
                log.warn("The user [0] fetched failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No user record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public User create(User user, String role) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = repo.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            if (userCtx.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                user.setOwnerUser(userCtx.get());
            }
            user.setRoles(Arrays.asList(getRole(role)));
            user.setPassword(encoder.encode(user.getPassword()));
            try {
                User entity = repo.save(user);
                if (entity != null) {
                    log.info("The user [{}] created successfully.", entity.getId());
                } else {
                    log.info("The user [{}] creation failed.", user.getUsername());
                }
                return entity;
            } catch (Exception ex) {
                log.error("The error occurred while creating new user and its information. SQL Error [{}]", ex.getMessage());
                throw new RecordConflictException("The error occurred while creating new user and its information. SQL Error :: [" + ex.getMessage() + "]");
            }

        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public User update(User user, String role) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = repo.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            Optional<User> entity = repo.findById(user.getId());
            if (entity.isPresent()) {
                try {
                    user.setRoles(Arrays.asList(getRole(role)));
                    user.setPassword(entity.get().getPassword());
                    user.setEnabled(entity.get().isEnabled());
                    user.setEmailVerified(entity.get().isEmailVerified());
                    user.setUserType(entity.get().getUserType());
                    User newEntity = repo.save(user);

                    if (newEntity != null) {
                        log.info("The user [{}] updated successfully.", newEntity.getId());
                    } else {
                        log.info("The user [{}] update failed.", entity.get().getUsername());
                    }
                    return newEntity;
                } catch (Exception e) {
                    log.error("The provided username/email: [{}] already exists.", user.getUsername());
                    throw new RecordConflictException("The provided username: [" + user.getUsername() +
                            "] or email: [" + user.getEmail() + "] already exists.");
                }
            } else {
                log.warn("The user [0] update failed. The provided Id: [{}] is not found.", user.getId());
                throw new RecordNotFoundException("No user record exists for given Id: [" + user.getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public User assignEmployeeRetailer(Long id, List<Long> retailerIds) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = repo.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            Optional<User> infoEntity = repo.findById(id);
            if (infoEntity.isPresent()) {
                if (retailerIds == null) {
                    return null;
                } else {
                    List<User> retailerEntity = repo.findAllById(retailerIds);

                    infoEntity.get().setRetailers(retailerEntity);
                    User registeredUser = repo.save(infoEntity.get());

                    return registeredUser;
                }
            } else {
                log.error("The user [0] fetched failed. The provided user info: [{}] is not found.", id);
                throw new RecordConflictException("No user record exists for given username: [" + securityContext.getUsername() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public User deleteSoft(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = repo.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<User> entity = repo.findById(id);

            if (entity.isPresent()) {
                User newEntity = entity.get();
                newEntity.setEnabled(false);
                newEntity.setTokenExpired(true);
                newEntity.setDeletedDate(LocalDateTime.now());
                newEntity = repo.save(newEntity);

                if (newEntity != null) {
                    log.info("The user [{}] soft delete successfully.", newEntity.getId());
                } else {
                    log.info("The user [{}] soft delete failed.", id);
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

    @Override
    public User delete(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = repo.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {

            Optional<User> newEntity = repo.findById(id);

            if (newEntity.isPresent()) {
                repo.deleteById(id);
                log.info("The user [{}] deleted successfully.", id);
                return newEntity.get();
            } else {
                log.warn("The user [0] delete failed. The provided Id: [{}] is not found.", id);
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
            case "WHOLESALER":
                return roleRepository.findByName(ERole.ROLE_WHOLESALER.name())
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
