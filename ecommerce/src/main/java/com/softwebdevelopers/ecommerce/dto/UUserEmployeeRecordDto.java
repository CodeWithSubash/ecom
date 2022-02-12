package com.softwebdevelopers.ecommerce.dto;

import com.softwebdevelopers.ecommerce.models.custom.ProductCount;
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
public class UUserEmployeeRecordDto {

//    private UUserEmployeeDto userDetail;

    private Long userId;

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String designation;

    private String phone;

    private String mobile;

    private String city;

    private String state;

    private String zipCode;

    private float commission;

    private int totalOrder;

    private float totalRevenue;

    private float totalAveragePrice;

    private float currentRevenue;

    private int currentOrder;

    private float previousRevenue;

    private int previousOrder;

    private float currentEarning;

    private float previousEarning;

    private List<ProductCount> monthlyGraph;
}
