package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.PCategoryRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IPCategoryService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PCategoryServiceImpl implements IPCategoryService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    PCategoryRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public List<Category> getAll(String type) throws RecordNotFoundException {

        List<Category> categoryList = type.equalsIgnoreCase("parent")
                ? repo.getAllParent() : type.equalsIgnoreCase("subchild")
                ? repo.getAllSubChild() : repo.findAll();
        if (Preconditions.checkNotNull(categoryList)) {
            log.info("The category list returned successfully.");
            return categoryList;
        } else {
            log.warn("The category [0] fetched failed. The category list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Category> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Category> categoryList = null;
        if(Preconditions.checkNotBlank(page.getKeyword())) {
            categoryList = repo.findByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            categoryList = repo.findAll(indexPageWithElements);
        }

        if (Preconditions.checkNotNull(categoryList)) {
            log.info("The category list returned successfully.");
            return categoryList;
        } else {
            log.warn("The category [0] fetched failed. The category list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Category> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Category> categoryList = null;
        if(Preconditions.checkNotBlank(page.getKeyword())) {
            categoryList = repo.getAllActiveByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            categoryList = repo.getAllActive(indexPageWithElements);
        }
        if (Preconditions.checkNotNull(categoryList)) {
            log.info("The active category list returned successfully.");
            return categoryList;
        } else {
            log.warn("The category [0] fetched failed. The active category list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Category> getAllInactive(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Category> categoryList = null;
        if(Preconditions.checkNotBlank(page.getKeyword())) {
            categoryList = repo.getAllInactiveByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            categoryList = repo.getAllInactive(indexPageWithElements);
        }
        if (Preconditions.checkNotNull(categoryList)) {
            log.info("The inactive category list returned successfully.");
            return categoryList;
        } else {
            log.warn("The category [0] fetched failed. The inactive category list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Category getById(Long id) throws RecordNotFoundException {
        Optional<Category> category = repo.findById(id);

        if (category.isPresent()) {
            log.info("The category with Id: [{}] returned successfully.", id);
            return category.get();
        } else {
            log.warn("The category [0] fetched failed. The provided Id: [{}] is not found.", id);
            throw new RecordNotFoundException("No category record exists for given Id: [" + id + "].");
        }
    }

    @Override
    public Category create(Category category) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            category.setCreatedUser(user.get());
            Category entity = repo.save(category);

            if (entity != null) {
                log.info("The category [{}] created successfully.", entity.getId());
            } else {
                log.info("The category [{}] creation failed.", category.getName());
            }
            return entity;
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Category update(Category category) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            Optional<Category> entity = repo.findById(category.getId());
            if (entity.isPresent()) {
                category.setCreatedUser(entity.get().getCreatedUser());
                category.setUpdatedUser(user.get());
                Category newEntity = repo.save(category);

                if (newEntity != null) {
                    log.info("The category [{}] updated successfully.", newEntity.getId());
                } else {
                    log.info("The category [{}] update failed.", category.getName());
                }
                return newEntity;
            } else {
                log.warn("The category [0] update failed. The provided Id: [{}] is not found.", category.getId());
                throw new RecordNotFoundException("No category record exists for given Id: [" + category.getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Category deleteSoft(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<Category> entity = repo.findById(id);

            if (entity.isPresent()) {
                Category newEntity = entity.get();
                newEntity.setUpdatedUser(user.get());
                newEntity.setDeletedDate(LocalDateTime.now());
                newEntity = repo.save(newEntity);

                if (newEntity != null) {
                    log.info("The category [{}] soft delete successfully.", newEntity.getId());
                } else {
                    log.info("The category [{}] soft delete failed.", id);
                }
                return newEntity;
            } else {
                log.warn("The category [0] update failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No category record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
