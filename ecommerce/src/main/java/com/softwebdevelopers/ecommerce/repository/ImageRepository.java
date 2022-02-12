package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.common.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT * FROM images WHERE model_id = ? AND model_type = ?", nativeQuery = true)
    Optional<Image> getByModelId(Long modelId, String modelType);

    @Query(value = "SELECT * FROM images WHERE model_id = ? AND model_type = ?", nativeQuery = true)
    Optional<List<Image>> getAllByModelId(Long modelId, String modelType);
}
