package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PagingSortingAndSearchDto {

    private int pageNo;

    private int pageSize;

    private String keyword;

    private String orderBy;

    private String orderType;
}
