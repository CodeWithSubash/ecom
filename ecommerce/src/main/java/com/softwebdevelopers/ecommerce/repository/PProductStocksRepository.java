package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.product.ProductStocks;
import com.softwebdevelopers.ecommerce.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PProductStocksRepository extends JpaRepository<ProductStocks, Long> {

    @Query(value = "SELECT * FROM product_stocks WHERE deleted_date IS NULL AND product_id = ?1 AND name LIKE %?2%",
            countQuery = "SELECT count(*) FROM product_stocks WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<ProductStocks> getAllByProductIdAndKeywordWithPagination(Long productId, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM product_stocks WHERE deleted_date IS NULL AND product_id = ?1",
            countQuery = "SELECT count(*) FROM product_stocks WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<ProductStocks> getAllByProductIdWithPagination(Long productId, Pageable pageable);

    @Query(value = "SELECT * FROM product_stocks WHERE id = (SELECT MAX(id) FROM product_stocks WHERE product_id = ?)", nativeQuery = true)
    Optional<ProductStocks> findMaxByProductId(Long productId);

    Page<ProductStocks> findAllByProductIdAndCreatedDateLessThanEqualAndCreatedDateGreaterThanEqualAndDeletedDateIsNull(Long productId, LocalDateTime endDate, LocalDateTime startDate, Pageable pageable);
}
