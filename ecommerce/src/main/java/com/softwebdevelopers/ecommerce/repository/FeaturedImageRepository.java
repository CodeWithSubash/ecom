package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.common.FeaturedImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturedImageRepository extends JpaRepository<FeaturedImage, Long> {

    @Query(value = "SELECT * FROM featured_images WHERE title LIKE %?1% OR sub_title LIKE %?1% OR description LIKE %?1% OR featured_type LIKE %?1%",
            countQuery = "SELECT count(*) FROM featured_images",
            nativeQuery = true)
    Page<FeaturedImage> findByKeywordWithPagination(String keyword, Pageable pageable);
}
