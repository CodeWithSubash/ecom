package com.softwebdevelopers.ecommerce.dto.response;

import com.softwebdevelopers.ecommerce.dto.RRoleDto;
import com.softwebdevelopers.ecommerce.dto.UUserEmployeeDto;
import com.softwebdevelopers.ecommerce.dto.UUserRetailerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;

    private String name;

    private String email;

    private Collection<RRoleDto> roles;

    private String role;

    private UUserEmployeeDto userEmployee;

    private UUserRetailerDto userRetailer;


}
