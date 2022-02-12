package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.cart.Wishlist;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.*;
import com.softwebdevelopers.ecommerce.services.IPWishlistService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PWishlistServiceImpl implements IPWishlistService {
    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    PWishlistRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Autowired
    PProductRepository productRepo;

    @Override
    public List<Wishlist> getAll() throws RecordNotFoundException {
        return null;
    }

    @Override
    public List<Wishlist> getAllByUserId() throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            List<Wishlist> entity = repo.findByUserIdAndDeletedDateIsNull(user.get().getId());

            if (Preconditions.checkNotNull(entity)) {
                log.info("The wishlist with user Id: [{}] returned successfully.", user.get().getId());
                return entity;
            } else {
                log.warn("The wishlist [0] fetched failed. The provided user Id: [{}] is not found.", user.get().getId());
                throw new RecordNotFoundException("No wishlist record exists for given user Id: [" + user.get().getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Wishlist create(Long productId) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<Product> product = productRepo.findById(productId);
            if (product.isPresent()) {
                Optional<Wishlist> wishlist = repo.findByUserIdAndProductIdAndDeletedDateIsNull(user.get().getId(), productId);
                if (wishlist.isPresent()) {
                    log.info("The product with Id: [{}] is already added in wishlist.", productId);
                    throw new RecordNotFoundException(
                            "The product with Id: [" + productId + "] is already added in wishlist.");
                }
                Wishlist entity = new Wishlist().toBuilder()
                                .user(user.get())
                                        .product(product.get())
                                                .build();
                entity = repo.save(entity);
                if (entity != null) {
                    log.info("The wishlist [{}] created successfully.", entity.getId());
                } else {
                    log.info("The wishlist creation failed for product [{}].", productId);
                }
                return entity;
            } else {
                log.warn("The product [0] fetched failed. The provided product: [{}] is not found.",
                        productId);
                throw new RecordNotFoundException(
                        "No product record exists for given Id: [" + productId + "].");
            }

        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Wishlist deleteSoftByProductId(Long productId) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<Wishlist> entity = repo.findByUserIdAndProductIdAndDeletedDateIsNull(user.get().getId(), productId);

            if (entity.isPresent()) {
                entity.get().setDeletedDate(LocalDateTime.now());
                Wishlist newEntity = repo.save(entity.get());

                if (newEntity != null) {
                    log.info("The wishlist [{}] soft delete successfully.", newEntity.getId());
                } else {
                    log.info("The wishlist with product id [{}] soft delete failed.", productId);
                }
                return newEntity;
            } else {
                log.warn("The wishlist [0] update failed. The provided Id: [{}] is not found.", productId);
                throw new RecordNotFoundException("No wishlist record exists for given Id: [" + productId + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Wishlist deleteSoft(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<Wishlist> entity = repo.findById(id);

            if (entity.isPresent()) {
                entity.get().setDeletedDate(LocalDateTime.now());
                Wishlist newEntity = repo.save(entity.get());

                if (newEntity != null) {
                    log.info("The wishlist [{}] soft delete successfully.", newEntity.getId());
                } else {
                    log.info("The wishlist Id [{}] soft delete failed.", id);
                }
                return newEntity;
            } else {
                log.warn("The wishlist [0] update failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No wishlist record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
