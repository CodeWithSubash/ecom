package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.exceptions.RecordConflictException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.PProductRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IPProductService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PProductServiceImpl implements IPProductService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    PProductRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public List<Product> getAll() throws RecordNotFoundException {
        List<Product> productList = repo.findAll();
        if (Preconditions.checkNotNull(productList)) {
            log.info("The product list returned successfully.");
            return productList;
        } else {
            log.warn("The product [0] fetched failed. The product list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Product> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Product> productList = null;
        if (Preconditions.checkNotBlank(page.getKeyword())) {
            productList = repo.findByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            productList = repo.findAll(indexPageWithElements);
        }

        if (Preconditions.checkNotNull(productList)) {
            log.info("The product list returned successfully.");
            return productList;
        } else {
            log.warn("The product [0] fetched failed. The product list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Product> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException {

        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            Page<Product> productList = null;

            if (Preconditions.checkNotBlank(page.getKeyword())) {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                    productList = repo.getAllActiveByKeywordAndWholesalerIdWithPagination(user.get().getId(), page.getKeyword(), indexPageWithElements);
                } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                        || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))) {
                    productList = repo.getAllActiveByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
                }
            } else {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                    productList = repo.getAllActiveAndWholesalerId(user.get().getId(), indexPageWithElements);
                } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                        || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))) {
                    productList = repo.getAllActive(indexPageWithElements);
                }
            }

            if (Preconditions.checkNotNull(productList)) {
                log.info("The active product list returned successfully.");
                return productList;
            } else {
                log.warn("The product [0] fetched failed. The active product list are not found.");
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
    public Page<Product> getAllByCategory(PagingSortingAndSearchDto page, Long categoryId) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Product> productList = null;
        if (Preconditions.checkNotBlank(page.getKeyword()) && categoryId != null && categoryId != 0) {
            productList = repo.getAllCategoryAndKeywordWithPagination(categoryId, page.getKeyword(), indexPageWithElements);
        } else if (categoryId != null && categoryId != 0) {
            productList = repo.getAllByCategory(categoryId, indexPageWithElements);
        } else if (Preconditions.checkNotBlank(page.getKeyword())) {
            productList = repo.getAllActiveByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            productList = repo.getAllActive(indexPageWithElements);
        }

        if (Preconditions.checkNotNull(productList)) {
            log.info("The active product list returned successfully.");
            return productList;
        } else {
            log.warn("The product [0] fetched failed. The active product list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<Product> getAllByPopularity(PagingSortingAndSearchDto page, Long categoryId) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize());

        Page<Product> productList = null;
        if (Preconditions.checkNotBlank(page.getKeyword()) && categoryId != null && categoryId != 0) {
            productList = repo.getAllFilterByPopularityAndCategoryAndKeywordWithPagination(categoryId, page.getKeyword(), indexPageWithElements);
        } else if (categoryId != null && categoryId != 0) {
            productList = repo.getAllFilterByPopularityAndCategoryWithPagination(categoryId, indexPageWithElements);
        } else if (Preconditions.checkNotBlank(page.getKeyword())) {
            productList = repo.getAllFilterByPopularityAndKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            productList = repo.getAllFilterByPopularityWithPagination(indexPageWithElements);
        }

        if (Preconditions.checkNotNull(productList)) {
            log.info("The active product list returned successfully.");
            return productList;
        } else {
            log.warn("The product [0] fetched failed. The active product list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Product getById(Long id) throws RecordNotFoundException {
        Optional<Product> product = repo.findById(id);

        if (product.isPresent()) {
            log.info("The product with Id: [{}] returned successfully.", id);
            return product.get();
        } else {
            log.warn("The product [0] fetched failed. The provided Id: [{}] is not found.", id);
            return null;
//            throw new RecordNotFoundException("No product record exists for given Id: [" + id + "].");
        }
    }

    @Override
    public Product create(Product product, Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            if (id == 0 &&
                    user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                product.setWholesaler(user.get());
            } else if (id != 0 &&
                    user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()) ||
                            item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))) {
                product.setWholesaler(userRepository.getById(id));
            } else {
                log.warn("The product owner [0] fetched failed. The provided Id: [{}] is not found.", id);
                throw new ECOMException(
                        "No user is authorized to create product for given username: [" + securityContext.getUsername() + "].");
            }
            product.setCreatedUser(user.get());
            Product entity = repo.save(product);

            if (entity != null) {
                log.info("The product [{}] created successfully.", entity.getId());
            } else {
                log.info("The product [{}] creation failed.", product.getName());
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
    public Product update(Product product, Long wholesalerId) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            Optional<Product> entity = repo.findById(product.getId());
            if (entity.isPresent()) {
                if (wholesalerId == 0 &&
                        user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))
                        && entity.get().getWholesaler().equals(user.get())) {
                    product.setWholesaler(user.get());
                } else if (wholesalerId != 0 &&
                        user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()) ||
                                item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))) {
                    product.setWholesaler(userRepository.getById(wholesalerId));
                } else {
                    log.warn("The product owner [0] fetched failed. The provided Id: [{}] is not found.", wholesalerId);
                    throw new ECOMException(
                            "No user is authorized to update product for given username: [" + securityContext.getUsername() + "].");
                }

                product.setSlug(entity.get().getSlug());
                product.setAvailableStock(entity.get().getAvailableStock());
                product.setUpdatedUser(user.get());
                Product newEntity = repo.save(product);

                if (newEntity != null) {
                    log.info("The product [{}] updated successfully.", newEntity.getId());
                } else {
                    log.info("The product [{}] update failed.", product.getName());
                }
                return newEntity;
            } else {
                log.warn("The product [0] update failed. The provided Id: [{}] is not found.", product.getId());
                throw new RecordNotFoundException("No product record exists for given Id: [" + product.getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Product deleteSoft(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<Product> entity = repo.findById(id);

            if (entity.isPresent()) {
                Product newEntity = entity.get();
                newEntity.setUpdatedUser(user.get());
                newEntity.setDeletedDate(LocalDateTime.now());
                newEntity = repo.save(newEntity);

                if (newEntity != null) {
                    log.info("The product [{}] soft delete successfully.", newEntity.getId());
                } else {
                    log.info("The product [{}] soft delete failed.", id);
                }
                return newEntity;
            } else {
                log.warn("The product [0] update failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No product record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

//    @Override
//    public Product addToWishList(Long id) throws RecordNotFoundException {
//        securityContext = new ECOMSecurityContextHolder();
//        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
//
//        if (user.isPresent()) {
//            Optional<Product> entity = repo.findById(id);
//            if (entity.isPresent()) {
//                entity.get().setUsers(Arrays.asList(user.get()));
//                Product newEntity = repo.save(entity.get());
//
//                return newEntity;
//            } else {
//                log.error("The product [0] fetched failed. The provided product Id: [{}] is not found.", id);
//                throw new RecordConflictException("No product record exists for given Id: [" + id + "].");
//            }
//        } else {
//            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
//                    securityContext.getUsername());
//            throw new RecordNotFoundException(
//                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
//        }
//    }
//
//    @Override
//    public Product removeFromWishList(Long id) throws RecordNotFoundException {
//        securityContext = new ECOMSecurityContextHolder();
//        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
//
//        if (user.isPresent()) {
//            Optional<Product> newEntity = repo.findById(id);
//
//            if (newEntity.isPresent()) {
//                repo.deleteById(id);
//                log.info("The product [{}] deleted successfully from wishlist.", id);
//                return newEntity.get();
//            } else {
//                log.warn("The product [0] delete failed. The provided Id: [{}] is not found.", id);
//                throw new RecordNotFoundException("No product record exists for given Id: [" + id + "].");
//            }
//        } else {
//            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
//                    securityContext.getUsername());
//            throw new RecordNotFoundException(
//                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
//        }
//    }
}
