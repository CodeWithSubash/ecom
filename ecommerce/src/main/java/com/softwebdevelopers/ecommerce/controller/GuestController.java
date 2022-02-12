package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + GuestController.UNAUTHENTICATED_PATH)
public class GuestController {

    public static final String UNAUTHENTICATED_PATH = "/guest";
}
