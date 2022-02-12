package com.softwebdevelopers.ecommerce.dto.response;

import com.softwebdevelopers.ecommerce.dto.UploadFileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;

    private Long wholesalerId;

    private String wholesalerName;

    private String productName;

    private String categoryName;

    private String brandName;

    private float mrp;

    private float price;

    private List<UploadFileResponseDto> files;
}
