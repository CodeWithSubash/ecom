package com.softwebdevelopers.ecommerce.models.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RetailerCount {

    private Integer oYear;

    private Integer oMonth;

    private String monthName;

    private Integer countOrder;

    private Float totalAmount;
}
