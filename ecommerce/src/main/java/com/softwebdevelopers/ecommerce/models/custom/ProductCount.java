package com.softwebdevelopers.ecommerce.models.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductCount {

    private Integer oYear;

    private Integer oMonth;

    private String monthName;

    private Long productId;

    private String productName;

    private Integer productCount;
}
