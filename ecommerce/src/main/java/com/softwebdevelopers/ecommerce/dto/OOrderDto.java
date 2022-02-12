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
public class OOrderDto {

    private Long id;

    private String billNumber;

    private String billingAddress;

    private float totalAmount;

    private float discountAmount;

    private float deliveryCharge;

    private float totalVat;

    private float grandTotalAmount;

    private float amountPaid;

    private float amountRemaining;

    private String deliveryStatus;

    private String paymentStatus;

    private String paymentMethod;

    private String orderTrackingNumber;

    private String deliveryType;

    private String notes;

    private UUserDto user;

    private List<OOrderDetailDto> orderDetails;

    private String createdUser;

    private LocalDateTime createdAt;
}
