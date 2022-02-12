package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PBrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByName(String name);

    @Query(value = "SELECT * FROM brands WHERE deleted_date IS NULL", nativeQuery = true)
    List<Brand> getAll();

    @Query(value = "SELECT * FROM brands WHERE name LIKE %?1%",
            countQuery = "SELECT count(*) FROM brands",
            nativeQuery = true)
    Page<Brand> findByKeywordWithPagination(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM brands WHERE deleted_date IS NULL",
            countQuery = "SELECT count(*) FROM brands WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Brand> getAllActive(Pageable pageable);

    @Query(value = "SELECT * FROM brands WHERE deleted_date IS NULL AND name LIKE %?1%",
            countQuery = "SELECT count(*) FROM brands WHERE deleted_date IS NULL",
            nativeQuery = true)
    Page<Brand> getAllActiveByKeywordWithPagination(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM brands WHERE deleted_date IS NOT NULL",
            countQuery = "SELECT count(*) FROM brands WHERE deleted_date IS NOT NULL",
            nativeQuery = true)
    Page<Brand> getAllInactive(Pageable pageable);

    @Query(value = "SELECT * FROM brands WHERE deleted_date IS NOT NULL AND name LIKE %?1%",
            countQuery = "SELECT count(*) FROM brands WHERE deleted_date IS NOT NULL",
            nativeQuery = true)
    Page<Brand> getAllInactiveByKeywordWithPagination(String keyword, Pageable pageable);
}
