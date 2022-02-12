package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OOrderDetailDto {

    private Long id;

    private Long productId;

    private float quantity;

    private float totalAmount;

    private float discountAmount;

    private float deliveryCharge;

    private float vat;

    private float grandTotalAmount;

    private PProductDto product;
}

