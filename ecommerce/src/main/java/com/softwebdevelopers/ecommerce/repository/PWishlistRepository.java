package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.cart.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PWishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserIdAndDeletedDateIsNull(Long userId);

    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);

    Optional<Wishlist> findByUserIdAndProductIdAndDeletedDateIsNull(Long userId, Long productId);
}
