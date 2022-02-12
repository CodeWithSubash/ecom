package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedImageDto {

    private Long id;

    private Long categoryId;

    private String title;

    private String subTitle;

    private String description;

    private String featuredType;

    private PCategoryDto category;

    private MultipartFile logoFile;

    private UploadFileResponseDto files;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;
}
