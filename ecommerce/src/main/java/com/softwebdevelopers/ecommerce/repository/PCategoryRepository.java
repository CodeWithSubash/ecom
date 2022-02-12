package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PCategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Query(value = "SELECT * FROM categories WHERE deleted_date IS NULL AND parent_id IS NULL", nativeQuery = true)
    List<Category> getAllParent();

    @Query(value = "SELECT * FROM categories WHERE deleted_date IS NULL AND parent_id IS NOT NULL", nativeQuery = true)
    List<Category> getAllSubChild();

    @Query(value = "SELECT * FROM categories WHERE name LIKE %?1%",
            countQuery = "SELECT count(*) FROM categories",
            nativeQuery = true)
    Page<Category> findByKeywordWithPagination(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM categories WHERE deleted_date IS NULL",
            countQuery = "SELECT count(*) FROM categories WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Category> getAllActive(Pageable pageable);

    @Query(value = "SELECT * FROM categories WHERE deleted_date IS NULL AND name LIKE %?1%",
            countQuery = "SELECT count(*) FROM categories WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Category> getAllActiveByKeywordWithPagination(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM categories WHERE deleted_date IS NOT NULL",
            countQuery = "SELECT count(*) FROM categories WHERE deleted_date IS NOT NULL",
            nativeQuery = true)
    Page<Category> getAllInactive(Pageable pageable);

    @Query(value = "SELECT * FROM categories WHERE deleted_date IS NOT NULL AND name LIKE %?1%",
            countQuery = "SELECT count(*) FROM categories WHERE deleted_date IS NOT NULL",
            nativeQuery = true)
    Page<Category> getAllInactiveByKeywordWithPagination(String keyword, Pageable pageable);

}
