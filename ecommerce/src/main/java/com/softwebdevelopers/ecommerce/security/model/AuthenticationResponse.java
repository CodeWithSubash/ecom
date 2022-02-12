package com.softwebdevelopers.ecommerce.security.model;

import com.softwebdevelopers.ecommerce.dto.UUserDto;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String accessToken;

    private String refreshToken;

    private UUserDto user;

    public AuthenticationResponse(String accessToken, String refreshToken, UUserDto user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }
}
