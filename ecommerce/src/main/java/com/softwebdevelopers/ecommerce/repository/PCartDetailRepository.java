package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.cart.Cart;
import com.softwebdevelopers.ecommerce.models.cart.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PCartDetailRepository extends JpaRepository<CartDetail, Long> {

//    @Query(value = "SELECT * FROM cart_details WHERE cart_id = ?1 AND product_id = ?2", nativeQuery = true)
    Optional<CartDetail> findByCartIdAndProductIdAndDeletedDateIsNull(Long cartId, Long productId);

}
