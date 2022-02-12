package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.common.FeaturedImage;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.FeaturedImageRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IFeaturedImageService;
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

@Slf4j
@Service
public class FeaturedImageServiceImpl implements IFeaturedImageService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    FeaturedImageRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public Page<FeaturedImage> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<FeaturedImage> featuredList = null;
        if(Preconditions.checkNotBlank(page.getKeyword())) {
            featuredList = repo.findByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            featuredList = repo.findAll(indexPageWithElements);
        }

        if (Preconditions.checkNotNull(featuredList)) {
            log.info("The featured List returned successfully.");
            return featuredList;
        } else {
            log.warn("The featured List [0] fetched failed. The brand list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public FeaturedImage getById(Long id) throws RecordNotFoundException {
        Optional<FeaturedImage> featured = repo.findById(id);
        if (featured.isPresent()) {
            log.info("The featured with Id: [{}] returned successfully.", id);
            return featured.get();
        } else {
            log.warn("The featured [0] fetched failed. The provided Id: [{}] is not found.", id);
            throw new RecordNotFoundException("No featured record exists for given Id: [" + id + "].");
        }
    }

    @Override
    public FeaturedImage create(FeaturedImage featured) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            FeaturedImage entity = repo.save(featured);

            if (entity != null) {
                log.info("The featured image [{}] created successfully.", entity.getId());
            } else {
                log.info("The featured image [{}] creation failed.", featured.getTitle());
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
    public FeaturedImage update(FeaturedImage featured) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            Optional<FeaturedImage> entity = repo.findById(featured.getId());
            if (entity.isPresent()) {
//                featured.setDeletedDate(entity.get().getDeletedDate());
                FeaturedImage newEntity = repo.save(featured);

                if (newEntity != null) {
                    log.info("The featured [{}] updated successfully.", newEntity.getId());
                } else {
                    log.info("The featured [{}] update failed.", featured.getTitle());
                }
                return newEntity;
            } else {
                log.warn("The featured [0] update failed. The provided Id: [{}] is not found.", featured.getId());
                throw new RecordNotFoundException("No featured record exists for given Id: [" + featured.getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public FeaturedImage publishedById(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<FeaturedImage> entity = repo.findById(id);

            if (entity.isPresent()) {
                FeaturedImage newEntity = entity.get();
                newEntity.setDeletedDate(Preconditions.checkNull(entity.get().getDeletedDate()) ? LocalDateTime.now() : null);
                newEntity = repo.save(newEntity);

                if (newEntity != null) {
                    log.info("The featured [{}] soft delete successfully.", newEntity.getId());
                } else {
                    log.info("The featured [{}] soft delete failed.", id);
                }
                return newEntity;
            } else {
                log.warn("The featured [0] update failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No featured record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public FeaturedImage delete(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {

            Optional<FeaturedImage> newEntity = repo.findById(id);

            if (newEntity.isPresent()) {
                repo.deleteById(id);
                log.info("The featured image [{}] deleted successfully.", id);
                return newEntity.get();
            } else {
                log.warn("The featured image [0] delete failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No featured image record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
