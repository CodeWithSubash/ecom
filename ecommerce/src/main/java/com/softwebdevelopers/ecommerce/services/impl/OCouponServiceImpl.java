package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.order.Coupon;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.OCouponRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IOCouponService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OCouponServiceImpl implements IOCouponService {
    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    OCouponRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public List<Coupon> getAll() throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {

            List<Coupon> couponList = null;
            if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                couponList = repo.findAllByUserId(user.get().getId());
            } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                    || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))) {
                couponList = repo.findAll();
            }

            if (Preconditions.checkNotNull(couponList)) {
                log.info("The coupon with user Id: [{}] returned successfully.", user.get().getId());
                return couponList;
            } else {
                log.warn("The coupon [0] fetched failed. The provided user Id: [{}] is not found.", user.get().getId());
                throw new RecordNotFoundException("No coupon record exists for given user Id: [" + user.get().getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<Coupon> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            Page<Coupon> couponList = null;

            if (Preconditions.checkNotBlank(page.getKeyword())) {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                    couponList = repo.findAllByUserIdAndCouponCodeContaining(user.get().getId(), page.getKeyword(), indexPageWithElements);
                } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                        || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))) {
                    couponList = repo.findAllByCouponCodeContaining(page.getKeyword(), indexPageWithElements);
                }
            } else {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                    couponList = repo.findAllByUserId(user.get().getId(), indexPageWithElements);
                } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                        || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))) {
                    couponList = repo.findAll(indexPageWithElements);
                }
            }

            if (Preconditions.checkNotNull(couponList)) {
                log.info("The coupon with user Id: [{}] returned successfully.", user.get().getId());
                return couponList;
            } else {
                log.warn("The coupon [0] fetched failed. The provided user Id: [{}] is not found.", user.get().getId());
                throw new RecordNotFoundException("No coupon record exists for given user Id: [" + user.get().getId() + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Coupon getById(UUID uuid) throws RecordNotFoundException {
        Optional<Coupon> coupon = repo.findById(uuid);

        if (coupon.isPresent()) {
            log.info("The coupon with Id: [{}] returned successfully.", uuid);
            return coupon.get();
        } else {
            log.warn("The coupon [0] fetched failed. The provided Id: [{}] is not found.", uuid);
            return null;
        }
    }

    @Override
    public Coupon getByCouponCode(String code) throws RecordNotFoundException {
        Optional<Coupon> coupon = repo.findByCouponCode(code);

        if (coupon.isPresent()) {
            log.info("The coupon with Code: [{}] returned successfully.", code);
            return coupon.get();
        } else {
            log.warn("The coupon [0] fetched failed. The provided Code: [{}] is not found.", code);
            return null;
        }
    }

    @Override
    public Coupon create(Coupon order) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                order.setUser(user.get());
            } else {
                log.info("The coupon [{}] creation failed.", order.getCouponCode());
                throw new ECOMException("The user with role [" + user.get().getRoles().stream().findFirst().get().getName() + "] is not authorized to create coupon.");
            }
            order.setRedeemAllowed(true);
            Coupon entity = repo.save(order);

            if (entity != null) {
                log.info("The coupon [{}] created successfully.", entity.getCouponCode());
            } else {
                log.info("The coupon [{}] creation failed.", order.getCouponCode());
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
    public Coupon update(Coupon order) throws RecordNotFoundException {
        return null;
    }

    @Override
    public Coupon dispose(String uuid) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {

            Optional<Coupon> entity = repo.findById(UUID.fromString(uuid));
            if (entity.isPresent()) {
                Coupon newEntity = entity.get();

                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))
                        && user.get().getId() == entity.get().getUser().getId()) {

                    newEntity.setRedeemAllowed(false);
                } else {
                    log.info("The coupon Id [{}] dispose failed.", entity.get().getCouponCode());
                    throw new ECOMException("The user with id [" + entity.get().getUser().getId() + "] is not authorized to dispose coupon.");
                }
                newEntity = repo.save(newEntity);

                if (newEntity != null) {
                    log.info("The coupon [{}] dispose successfully.", newEntity.getCouponCode());
                } else {
                    log.info("The coupon [{}] dispose failed.", uuid);
                }
                return newEntity;
            } else {
                log.warn("The coupon [0] update failed. The provided Id: [{}] is not found.", uuid);
                throw new RecordNotFoundException("No coupon record exists for given Id: [" + uuid + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Coupon delete(Long id) throws RecordNotFoundException {
        return null;
    }
}
