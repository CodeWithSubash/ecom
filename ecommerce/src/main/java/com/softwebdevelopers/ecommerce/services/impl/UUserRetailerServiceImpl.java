package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.UUserRetailerDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordConflictException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.custom.IRetailerCount;
import com.softwebdevelopers.ecommerce.models.custom.RetailerCount;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.models.user.UserEmployeeRecord;
import com.softwebdevelopers.ecommerce.models.user.UserRetailer;
import com.softwebdevelopers.ecommerce.models.user.UserRetailerRecord;
import com.softwebdevelopers.ecommerce.repository.OOrderRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRetailerRecordRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRetailersRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IUUserRetailerService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UUserRetailerServiceImpl implements IUUserRetailerService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    UUserRetailersRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Autowired
    UUserRetailerRecordRepository recordRepository;

    @Override
    public List<UserRetailer> getAll() throws RecordNotFoundException {
        List<UserRetailer> retailerList = repo.getAll();
        if (Preconditions.checkNotNull(retailerList)) {
            log.info("The retailer list returned successfully.");
            return retailerList;
        } else {
            log.warn("The retailer [0] fetched failed. The KYC list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public UserRetailer getById(Long id) throws RecordNotFoundException {
        Optional<UserRetailer> retailer = repo.findById(id);

        if (retailer.isPresent()) {
            log.info("The user retailer with Id: [{}] returned successfully.", id);
            return retailer.get();
        } else {
            log.warn("The user retailer [0] fetched failed. The provided Id: [{}] is not found.", id);
            throw new RecordNotFoundException("No retailer record exists for given Id: [" + id + "].");
        }
    }

    @Override
    public UserRetailerRecord getRetailerRecord() throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = userRepository.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            Optional<UserRetailerRecord> info = recordRepository.findByUserId(userCtx.get().getId());

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

    @Override
    public UserRetailer updateRetailerById(UUserRetailerDto retailer) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = userRepository.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            Optional<UserRetailer> entity = repo.findById(retailer.getId());
            if (entity.isPresent()) {
                try {
                    entity.get().setDeletedDate(retailer.isStatus() ? null : LocalDateTime.now());
                    entity.get().setStatus(retailer.isStatus());
                    entity.get().setNotes(retailer.getNotes());
                    UserRetailer newEntity = repo.save(entity.get());

                    if (newEntity != null) {
                        log.info("The user retailer [{}]  status updated successfully.", newEntity.getId());
                    } else {
                        log.info("The user retailer [{}] status update failed.", retailer.getId());
                    }
                    return newEntity;
                } catch (Exception e) {
                    log.error("Error exist while saving the status of retailer in User retailer [{}].", retailer.getId());
                    throw new RecordConflictException("The provided retailer id: [" + retailer.getId() + "] already exists.");
                }
            } else {
                log.warn("The user retailer [0] update failed. The provided Id: [{}] is not found.", retailer.getId());
                throw new RecordNotFoundException("No user retailer record exists for given Id: [" + retailer.getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public UserRetailer update(UserRetailer kyc) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> userCtx = userRepository.findByUsername(securityContext.getUsername());

        if (userCtx.isPresent()) {
            if (Preconditions.checkNotNull(userCtx.get().getUserRetailer())) {
                try {
                    kyc.setId(userCtx.get().getUserRetailer().getId());
                    kyc.setUser(userCtx.get());
                    kyc.setDeletedDate(null);
                    kyc.setStatus(false);
                    kyc.setNotes(userCtx.get().getUserRetailer().getNotes());
                    UserRetailer newEntity = repo.save(kyc);

                    if (newEntity != null) {
                        log.info("The user retailer [{}]  status updated successfully.", newEntity.getId());
                    } else {
                        log.info("The user retailer [{}] status update failed.", kyc.getId());
                    }
                    return newEntity;
                } catch (Exception e) {
                    log.error("Error exist while saving the info of retailer in User retailer [{}].", kyc.getId());
                    throw new RecordConflictException("The provided retailer id: [" + kyc.getId() + "] already exists.");
                }
            } else {
                log.warn("The user retailer [0] update failed. The provided Id: [{}] is not found.", kyc.getId());
                throw new RecordNotFoundException("No user retailer record exists for given Id: [" + kyc.getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
