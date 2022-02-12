package com.softwebdevelopers.ecommerce.dto;

import com.softwebdevelopers.ecommerce.models.custom.RetailerCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UUserRetailerRecordDto {

    private Long userId;

    private Long id;

    private String businessName;

    private int orderCount;

    private float totalAmount;

    private int wishlistCount;

    private List<RetailerCount> monthlyGraph;
}
