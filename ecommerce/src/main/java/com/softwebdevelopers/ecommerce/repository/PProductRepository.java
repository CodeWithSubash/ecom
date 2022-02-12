package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    @Query(value = "SELECT * FROM products WHERE name LIKE %?1% OR description LIKE %?1%",
            countQuery = "SELECT count(*) FROM products",
            nativeQuery = true)
    Page<Product> findByKeywordWithPagination(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE deleted_date IS NULL",
            countQuery = "SELECT count(*) FROM products WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllActive(Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE deleted_date IS NULL AND (name LIKE %?1% OR description LIKE %?1%)",
            countQuery = "SELECT count(*) FROM products WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllActiveByKeywordWithPagination(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE user_id = ?1 AND deleted_date IS NULL",
            countQuery = "SELECT count(*) FROM products WHERE user_id = ?1 AND deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllActiveAndWholesalerId(Long wholesalerId, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE user_id = ?1 AND deleted_date IS NULL AND (name LIKE %?2% OR description LIKE %?2%)",
            countQuery = "SELECT count(*) FROM products WHERE user_id = ?1 AND deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllActiveByKeywordAndWholesalerIdWithPagination(Long wholesalerId, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE category_id = ?1 AND deleted_date IS NULL",
            countQuery = "SELECT count(*) FROM products WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllByCategory(Long categoryId, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE category_id = ?1 AND deleted_date IS NULL AND (name LIKE %?2% OR description LIKE %?2%)",
            countQuery = "SELECT count(*) FROM products WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllCategoryAndKeywordWithPagination(Long categoryId, String keyword, Pageable pageable);

    @Query(value = "SELECT p.* FROM products p LEFT JOIN (SELECT product_id, SUM(quantity) AS quantity FROM order_details GROUP BY product_id) qty ON qty.product_id = p.id WHERE p.deleted_date IS NULL ORDER BY qty.quantity DESC",
            countQuery = "SELECT count(*) FROM products WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllFilterByPopularityWithPagination(Pageable pageable);

    @Query(value = "SELECT p.* FROM products p LEFT JOIN (SELECT product_id, SUM(quantity) AS quantity FROM order_details GROUP BY product_id) qty ON qty.product_id = p.id WHERE p.deleted_date IS NULL AND (p.name LIKE %?1% OR description LIKE %?1%) ORDER BY qty.quantity DESC",
            countQuery = "SELECT count(*) FROM products WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllFilterByPopularityAndKeywordWithPagination(String keyword, Pageable pageable);

    @Query(value = "SELECT p.* FROM products p LEFT JOIN (SELECT product_id, SUM(quantity) AS quantity FROM order_details GROUP BY product_id) qty ON qty.product_id = p.id WHERE category_id = ?1 AND p.deleted_date IS NULL ORDER BY qty.quantity DESC",
            countQuery = "SELECT count(*) FROM products WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllFilterByPopularityAndCategoryWithPagination(Long categoryId, Pageable pageable);

    @Query(value = "SELECT p.* FROM products p LEFT JOIN (SELECT product_id, SUM(quantity) AS quantity FROM order_details GROUP BY product_id) qty ON qty.product_id = p.id WHERE category_id = ?1 AND p.deleted_date IS NULL AND (p.name LIKE %?2% OR description LIKE %?2%) ORDER BY qty.quantity DESC",
            countQuery = "SELECT count(*) FROM products WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Product> getAllFilterByPopularityAndCategoryAndKeywordWithPagination(Long categoryId, String keyword, Pageable pageable);
}
