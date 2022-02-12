package com.softwebdevelopers.ecommerce.security.resources;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.business.URegistrationBL;
import com.softwebdevelopers.ecommerce.dto.URegistrationDto;
import com.softwebdevelopers.ecommerce.dto.UUserDto;
import com.softwebdevelopers.ecommerce.exceptions.BadRequestException;
import com.softwebdevelopers.ecommerce.security.WebSecurityConfig;
import com.softwebdevelopers.ecommerce.security.model.AuthenticationRequest;
import com.softwebdevelopers.ecommerce.security.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + AuthResource.AUTHENTICATED_PATH)
public class AuthResource {

    public static final String AUTHENTICATED_PATH = "/auth";

    @Autowired
    AuthUserBL authService;

    @Autowired
    URegistrationBL registrationService;

    @PostMapping("/registration")
    public ResponseEntity<?> userRegistration(@RequestBody URegistrationDto registrationDto) {
        UUserDto user = registrationService.registerUser(registrationDto);
        return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody AuthenticationRequest authenticationRequest) {

        if (Preconditions.checkNull(authenticationRequest.getUsername()) || Preconditions.checkBlank(authenticationRequest.getUsername())) {
            throw new BadRequestException("Username can't be null or blank.");
        }

        if (Preconditions.checkNull(authenticationRequest.getPassword()) || Preconditions.checkBlank(authenticationRequest.getPassword())) {
            throw new BadRequestException("Password can't be null or blank.");
        }

        AuthenticationResponse result = authService.authentication(authenticationRequest);
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }
}
