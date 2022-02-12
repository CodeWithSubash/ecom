package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.order.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OCouponRepository extends JpaRepository<Coupon, UUID> {

    List<Coupon> findAllByUserId(Long userId);

    Page<Coupon> findAllByUserId(Long userId, Pageable pageable);

    Optional<Coupon> findByCouponCode(String keyword);

    Page<Coupon> findAllByCouponCodeContaining(String keyword, Pageable pageable);

    Page<Coupon> findAllByUserIdAndCouponCodeContaining(Long userId, String keyword, Pageable pageable);
}
