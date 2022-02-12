package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaginationInfoDto {

    private int size;

    private int numberOfItems;

    private int currentPage;

    private Long totalItems;

    private int totalPages;

    private boolean isFirst;

    private boolean isLast;

    private boolean hasNext;

    private boolean hasPrevious;
}
