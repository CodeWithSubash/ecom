package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.cart.Cart;
import com.softwebdevelopers.ecommerce.models.cart.CartDetail;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.PCartDetailRepository;
import com.softwebdevelopers.ecommerce.repository.PCartRepository;
import com.softwebdevelopers.ecommerce.repository.PProductRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IPCartService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PCartServiceImpl implements IPCartService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    PCartRepository repo;

    @Autowired
    PCartDetailRepository cartDetailRepo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public List<Cart> getAll() throws RecordNotFoundException {
        return null;
    }

    @Override
    public Cart getAllByUserId() throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
//            Optional<Cart> entity = repo.findByUserIdAndCreatedUserId(user.get().getId(), user.get().getId());
            Optional<Cart> entity = repo.findByUserIdAndDeletedDateIsNull(user.get().getId());

            if (entity.isPresent()) {
                log.info("The cart with Id: [{}] returned successfully.", entity.get().getId());
                return entity.get();
            } else {
                log.warn("The cart [0] fetched failed. The provided Id: [{}] is not found.", user.get().getId());
                throw new RecordNotFoundException("No cart record exists for given Id: [" + user.get().getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Cart getAllByCreatedUserId(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
//            Optional<Cart> entity = repo.findByUserIdAndCreatedUserId(id, user.get().getId());
            Optional<Cart> entity = repo.findByUserIdAndDeletedDateIsNull(id);

            if (entity.isPresent()) {
                log.info("The cart with Id: [{}] returned successfully.", entity.get().getId());
                return entity.get();
            } else {
                log.warn("The cart [0] fetched failed. The provided Id: [{}] is not found.", user.get().getId());
                throw new RecordNotFoundException("No user record exists for given Id: [" + user.get().getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public CartDetail create(CartDetail cartDetail, Long userId) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            //Created By Sales Person
            if (userId != null && userId != 0) {
//                Optional<Cart> cartInfo = repo.findByUserIdAndCreatedUserId(userId, user.get().getId());
                Optional<Cart> cartInfo = repo.findByUserIdAndDeletedDateIsNull(userId);
                if (cartInfo.isPresent()) {
                    Optional<CartDetail> oldDetail = cartDetailRepo.findByCartIdAndProductIdAndDeletedDateIsNull(cartInfo.get().getId(), cartDetail.getProduct().getId());
                    if(oldDetail.isPresent()) {
                        cartDetail.setId(oldDetail.get().getId());
                    }
                    cartDetail.setCart(cartInfo.get());
                    cartDetail = cartDetailRepo.save(cartDetail);
                    if (cartDetail != null) {
                        log.info("The cart detail [{}] created successfully.", cartDetail.getId());
                    } else {
                        log.info("The cart detail [{}] creation failed.", cartDetail.getId());
                    }
                    return cartDetail;
                } else {
                    Cart entity = new Cart().toBuilder()
                            .user(userRepository.getById(userId))
                            .createdUser(user.get())
                            .build();
                    CartDetail detail = cartDetail;
                    detail.setCart(entity);
                    entity.setCartDetail(Arrays.asList(detail));
                    entity = repo.save(entity);
                    if (entity != null) {
                        log.info("The cart [{}] created successfully with cart detail.", entity.getId());
                    } else {
                        log.info("The cart [] creation failed.");
                    }
                    return entity.getCartDetail().stream().findFirst().get();
                }
            } else {
//                Optional<Cart> cartInfo = repo.findByUserIdAndCreatedUserId(user.get().getId(), user.get().getId());
                Optional<Cart> cartInfo = repo.findByUserIdAndDeletedDateIsNull(user.get().getId());
                if (cartInfo.isPresent()) {
                    Optional<CartDetail> oldDetail = cartDetailRepo.findByCartIdAndProductIdAndDeletedDateIsNull(cartInfo.get().getId(), cartDetail.getProduct().getId());
                    if(oldDetail.isPresent()) {
                        cartDetail.setId(oldDetail.get().getId());
                    }
                    cartDetail.setCart(cartInfo.get());
                    cartDetail = cartDetailRepo.save(cartDetail);
                    if (cartDetail != null) {
                        log.info("The cart detail [{}] created successfully.", cartDetail.getId());
                    } else {
                        log.info("The cart detail [{}] creation failed.", cartDetail.getId());
                    }
                    return cartDetail;
                } else {
                    Cart entity = new Cart().toBuilder()
                            .user(user.get())
                            .createdUser(user.get())
                            .build();
                    cartDetail.setCart(entity);
                    entity.setCartDetail(Arrays.asList(cartDetail));
                    entity = repo.save(entity);
                    if (entity != null) {
                        log.info("The cart [{}] created successfully with cart detail.", entity.getId());
                    } else {
                        log.info("The cart [] creation failed.");
                    }
                    return entity.getCartDetail().stream().findFirst().get();
                }
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public CartDetail update(CartDetail category) throws RecordNotFoundException {
        return null;
    }

    @Override
    public CartDetail delete(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {

            Optional<CartDetail> newEntity = cartDetailRepo.findById(id);

            if (newEntity.isPresent()) {
                cartDetailRepo.deleteById(id);
                log.info("The cart detail Id: [{}] deleted successfully.", id);
                return newEntity.get();
            } else {
                log.warn("The cart detail [0] delete failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No cart detail record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Cart deleteSoft(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {

            Optional<Cart> entity = repo.findById(id);
            if (entity.isPresent()) {
                Cart newEntity = entity.get();
                newEntity.setDeletedDate(LocalDateTime.now());

                Collection<CartDetail> detail = entity.get().getCartDetail();
                for(CartDetail idx : detail) {
                    idx.setDeletedDate(LocalDateTime.now());
                    idx.setCart(newEntity);
                }
                newEntity.setCartDetail(detail);
                newEntity = repo.save(newEntity);
                log.info("The cart Id: [{}] deleted successfully.", id);
                return newEntity;
            } else {
                log.warn("The cart [0] delete failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No cart record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
