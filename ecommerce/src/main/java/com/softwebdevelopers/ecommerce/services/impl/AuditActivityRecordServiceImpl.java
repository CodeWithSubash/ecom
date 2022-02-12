package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.AuditActivityRecord;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.AuditActivityRecordRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IAuditActivityRecordService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuditActivityRecordServiceImpl  implements IAuditActivityRecordService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    UUserRepository userRepository;

    @Autowired
    AuditActivityRecordRepository repo;

    @Override
    public AuditActivityRecord create(AuditActivityRecord entity) {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            try {
                entity.setUser(user.get());
                entity.setUsername(user.get().getUsername());
                return repo.save(entity);
            } catch (Exception e) {
                log.warn("The audit activity record creation failed for the provided method: [{}] in the class [{}].", entity.getMethod(), entity.getClazz());
                return null;
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<AuditActivityRecord> getAll(int page, int size) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page, size);
            Page<AuditActivityRecord> auditList = repo.findAllWithPagination(user.get().getId(), indexPageWithElements);

            if (auditList != null && !auditList.isEmpty()) {
                log.info("The audit activity record for user with Id: [{}] returned successfully.",
                        user.get().getId());
                return auditList;
            } else {
                log.warn(
                        "The audit activity record [0] fetched failed. The audit activity record for provided user with Id: [{}] is not found.",
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
    public Page<AuditActivityRecord> getByKeyword(String keyword, int page, int size) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page, size);
            Page<AuditActivityRecord> auditList = repo.findByKeywordWithPagination(user.get().getId(), keyword, indexPageWithElements);

            if (auditList != null && !auditList.isEmpty()) {
                log.info("The audit activity record for user with Id: [{}] returned successfully.",
                        user.get().getId());
                return auditList;
            } else {
                log.warn(
                        "The audit activity record [0] fetched failed. The audit activity record for provided user with Id: [{}] is not found.",
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
}
