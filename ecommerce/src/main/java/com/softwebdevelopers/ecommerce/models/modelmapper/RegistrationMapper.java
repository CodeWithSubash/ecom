package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.dto.URegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper {

    public User toEntity(URegistrationDto dto) {
        return new User().toBuilder()
                .name(dto.getName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .enabled(false)
                .isEmailVerified(false)
                .tokenExpired(true)
                .userType(dto.getUserType())
                .build();
    }
}
