package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.common.CheckInCheckOutLog;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.CheckInCheckOutLogRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.ICheckInCheckOutLogService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CheckInCheckOutLogServiceImpl implements ICheckInCheckOutLogService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    UUserRepository userRepository;

    @Autowired
    CheckInCheckOutLogRepository repo;


    @Override
    public CheckInCheckOutLog create(CheckInCheckOutLog entity, Long retailerUserId) {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            try {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                    if (user.get().getRetailers().stream().anyMatch(item -> item.getId() == retailerUserId)) {
                        entity.setUser(user.get());
                        entity.setRetailerUser(userRepository.getById(retailerUserId));
                        return repo.save(entity);
                    } else {
                        log.warn("The user Id [{}] is not authorized to log the check in time at retailer Id [{}] location.", user.get().getId(), retailerUserId);
                        throw new ECOMException("The user [" + user.get().getName() + "] is not authorized to log the check in time at retailer Id [" + retailerUserId + "] location.");
                    }
                } else {
                    log.warn("The user Id [{}] is not authorized to log the check in time.", user.get().getId());
                    throw new ECOMException("The user [" + user.get().getName() + "] with no SALES role is not authorized to log the check in time.");
                }
            } catch (Exception e) {
                log.warn("The check in time and location creation failed for the user Id: [{}].", user.get().getId());
                throw new ECOMException(e.getMessage());
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<CheckInCheckOutLog> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());


            Page<CheckInCheckOutLog> logList = null;
            if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                logList = repo.findByUserId(user.get().getId(), indexPageWithElements);
            } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                    || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                    || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                logList = repo.findAll(indexPageWithElements);
            } else {
                throw new ECOMException("The user is not authorized to get the check in and check out log records.");
            }

            if (Preconditions.checkNotNull(logList)) {
                log.info("The check in check out record for user with Id: [{}] returned successfully.",
                        user.get().getId());
                return logList;
            } else {
                log.warn(
                        "The check in check out record [0] fetched failed. The audit activity record for provided user with Id: [{}] is not found.",
                        user.get().getId());
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
    public Page<CheckInCheckOutLog> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<CheckInCheckOutLog> logList = null;

            try {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                    logList = repo.findAllByUserIdAndCheckInTimeGreaterThanEqualAndCheckInTimeLessThanEqual(user.get().getId(), startDate, endDate, indexPageWithElements);
                } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                        || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                        || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                    logList = repo.findAllByCheckInTimeGreaterThanEqualAndCheckInTimeLessThanEqual(startDate, endDate, indexPageWithElements);
                }

                if (Preconditions.checkNotNull(logList)) {
                    log.info("The check in log list returned successfully.");
                    return logList;
                } else {
                    log.warn("The check in log [0] fetched failed. The check in log list are not found.");
                    throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
                }
            } catch (Exception ex) {
                throw new ECOMException("Error while fetching data of check in log.");
            }

        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<CheckInCheckOutLog> getByKeyword(String keyword, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        return null;
    }

    @Override
    public Page<CheckInCheckOutLog> getByUserId(Long userId, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<CheckInCheckOutLog> logList = null;
            if ((user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))
                    && user.get().getId() == userId)
                    || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                    || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                    || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                logList = repo.findByUserId(userId, indexPageWithElements);
            } else {
                throw new ECOMException("The user is not authorized to get the check in and check out log records.");
            }

            if (Preconditions.checkNotNull(logList)) {
                log.info("The check in check out record for user with Id: [{}] returned successfully.",
                        user.get().getId());
                return logList;
            } else {
                log.warn(
                        "The check in check out record [0] fetched failed. The audit activity record for provided user with Id: [{}] is not found.",
                        user.get().getId());
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
    public CheckInCheckOutLog getById(UUID uuid) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<CheckInCheckOutLog> category = repo.findById(uuid);

            if (category.isPresent()) {
                log.info("The check in log with Id: [{}] returned successfully.", uuid);
                return category.get();
            } else {
                log.warn("The check in log [0] fetched failed. The provided Id: [{}] is not found.", uuid);
                throw new RecordNotFoundException("No check in log record exists for given Id: [" + uuid + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
