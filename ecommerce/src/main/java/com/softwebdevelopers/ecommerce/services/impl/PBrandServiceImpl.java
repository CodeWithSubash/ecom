package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.PBrandRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IPBrandService;
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
public class PBrandServiceImpl implements IPBrandService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    PBrandRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public List<Brand> getAll() throws RecordNotFoundException {
        List<Brand> brandList = repo.getAll();
        if (Preconditions.checkNotNull(brandList)) {
            log.info("The brand list returned successfully.");
            return brandList;
        } else {
            log.warn("The brand [0] fetched failed. The category list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Brand> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Brand> brandList = null;
        if(Preconditions.checkNotBlank(page.getKeyword())) {
            brandList = repo.findByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            brandList = repo.findAll(indexPageWithElements);
        }

        if (Preconditions.checkNotNull(brandList)) {
            log.info("The brand list returned successfully.");
            return brandList;
        } else {
            log.warn("The brand [0] fetched failed. The brand list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Brand> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Brand> brandList = null;
        if(Preconditions.checkNotBlank(page.getKeyword())) {
            brandList = repo.getAllActiveByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            brandList = repo.getAllActive(indexPageWithElements);
        }
        if (Preconditions.checkNotNull(brandList)) {
            log.info("The active brand list returned successfully.");
            return brandList;
        } else {
            log.warn("The brand [0] fetched failed. The active brand list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Brand> getAllInactive(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Brand> brandList = null;
        if(Preconditions.checkNotBlank(page.getKeyword())) {
            brandList = repo.getAllInactiveByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            brandList = repo.getAllInactive(indexPageWithElements);
        }
        if (Preconditions.checkNotNull(brandList)) {
            log.info("The inactive brand list returned successfully.");
            return brandList;
        } else {
            log.warn("The brand [0] fetched failed. The inactive brand list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Brand getById(Long id) throws RecordNotFoundException {
        Optional<Brand> brand = repo.findById(id);
        if (brand.isPresent()) {
            log.info("The brand with Id: [{}] returned successfully.", id);
            return brand.get();
        } else {
            log.warn("The brand [0] fetched failed. The provided Id: [{}] is not found.", id);
            throw new RecordNotFoundException("No brand record exists for given Id: [" + id + "].");
        }
    }

    @Override
    public Brand create(Brand brand) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            brand.setCreatedUser(user.get());
            Brand entity = repo.save(brand);

            if (entity != null) {
                log.info("The brand [{}] created successfully.", entity.getId());
            } else {
                log.info("The brand [{}] creation failed.", brand.getName());
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
    public Brand update(Brand brand) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            Optional<Brand> entity = repo.findById(brand.getId());
            if (entity.isPresent()) {
                brand.setCreatedUser(entity.get().getCreatedUser());
                brand.setUpdatedUser(user.get());
                Brand newEntity = repo.save(brand);

                if (newEntity != null) {
                    log.info("The brand [{}] updated successfully.", newEntity.getId());
                } else {
                    log.info("The brand [{}] update failed.", brand.getName());
                }
                return newEntity;
            } else {
                log.warn("The brand [0] update failed. The provided Id: [{}] is not found.", brand.getId());
                throw new RecordNotFoundException("No brand record exists for given Id: [" + brand.getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Brand deleteSoft(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<Brand> entity = repo.findById(id);

            if (entity.isPresent()) {
                Brand newEntity = entity.get();
                newEntity.setUpdatedUser(user.get());
                newEntity.setDeletedDate(LocalDateTime.now());
                newEntity = repo.save(newEntity);

                if (newEntity != null) {
                    log.info("The brand [{}] soft delete successfully.", newEntity.getId());
                } else {
                    log.info("The brand [{}] soft delete failed.", id);
                }
                return newEntity;
            } else {
                log.warn("The brand [0] update failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No brand record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
