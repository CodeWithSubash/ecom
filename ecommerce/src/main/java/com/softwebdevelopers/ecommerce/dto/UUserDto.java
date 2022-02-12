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
public class UUserDto {

    private Long id;

    private Long ownerUserId;

    private String name;

    private String username;

    private String email;

    private Boolean enabled;

    private Boolean isEmailVerified;

    private Collection<RRoleDto> roles;

    private String role;

    private String userType;

    private UUserEmployeeDto userEmployee;

    private UUserRetailerDto userRetailer;

    private List<UUserRetailerDto> assignedRetailers;
}
