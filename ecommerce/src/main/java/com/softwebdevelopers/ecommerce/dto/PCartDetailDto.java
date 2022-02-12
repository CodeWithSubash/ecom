package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PCartDetailDto {

    private Long id;

    private Long userId;

    private Long productId;

    private float quantity;

    private PProductDto product;
}
