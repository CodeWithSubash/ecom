package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.cart.Cart;
import com.softwebdevelopers.ecommerce.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PCartRepository extends JpaRepository<Cart, Long> {

//    @Query(value = "SELECT * FROM carts WHERE user_id = ?1 AND deleted_date IS NULL", nativeQuery = true)
    Optional<Cart> findByUserIdAndDeletedDateIsNull(Long userId);

    @Query(value = "SELECT * FROM carts WHERE created_by = ? AND deleted_date IS NULL", nativeQuery = true)
    Optional<List<Cart>> findByCreatedUserId(Long createdUserId);

    @Query(value = "SELECT * FROM carts WHERE user_id = ?1 AND created_by = ?2 AND deleted_date IS NULL", nativeQuery = true)
    Optional<Cart> findByUserIdAndCreatedUserId(Long userId, Long createdUserId);
}
