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
public class PWishlistDto {

    private Long id;

    private Long userId;

    private String name;

    private PProductDto product;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;
}
