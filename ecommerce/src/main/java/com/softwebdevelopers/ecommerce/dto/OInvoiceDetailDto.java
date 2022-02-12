package com.softwebdevelopers.ecommerce.dto;

import com.softwebdevelopers.ecommerce.dto.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OInvoiceDetailDto {

    private Long id;

    private float quantity;

    private float grandTotalAmount;

    private ProductResponseDto product;
}
