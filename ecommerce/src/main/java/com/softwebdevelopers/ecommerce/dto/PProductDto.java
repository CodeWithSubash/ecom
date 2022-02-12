package com.softwebdevelopers.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PProductDto {

    private Long id;

    private Long wholesalerId;

    private String wholesalerName;

    private Long categoryId;

    private Long brandId;

    private String name;

    private String slug;

    private float mrp;

    private float price;

    private String description;

    private String shippingAndDelivery;

    private String paymentAndReturn;

    private String productCondition;

    private float availableStock;

    private float openingStock;

    private boolean deal;

    private List<String> tags;

    private PCategoryDto category;

    private PBrandDto brand;

    private List<PProductStockDto> productStocks;

    private MultipartFile[] images;

    private List<UploadFileResponseDto> files;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
