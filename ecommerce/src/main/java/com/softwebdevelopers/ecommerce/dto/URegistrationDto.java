package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class URegistrationDto {

    private Long id;

    private String name;

    private String username;

    private String oldPassword;

    private String password;

    private String rePassword;

    private String email;

    private String userType;

    private Collection<String> role;

    private UUserRetailerDto userRetailer;
}
