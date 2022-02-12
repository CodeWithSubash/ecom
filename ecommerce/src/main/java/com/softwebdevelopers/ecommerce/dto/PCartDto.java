package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PCartDto {

    private Long id;

    private Collection<PCartDetailDto> cartDetail;

    private UUserDto user;

    private String userName;

    private String createdUser;
}
