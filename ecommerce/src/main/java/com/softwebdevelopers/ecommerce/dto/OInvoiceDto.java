package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OInvoiceDto {

    private Long id;

    private String billNumber;

    private String retailerName;

    private String billingAddress;

    private float totalAmount;

    private float deliveryCharge;

    private float totalVat;

    private float grandTotalAmount;

    private String paymentStatus;

    private String paymentMethod;

    private List<OPaymentDto> payments;

    private List<OInvoiceDetailDto> orderDetails;

    private LocalDateTime createdAt;
}
