package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UUserEmployeeDto {

    private Long id;

    private Long userId;

    private String username;

    private String firstname;

    private String lastname;

    private String designation;

    private String phone;

    private String mobile;

    private String email;

    private String alternativePhone;

    private String alternativeMobile;

    private String street;

    private String street2;

    private String city;

    private String state;

    private String zipCode;

    private float commission;

    private String role;
}
