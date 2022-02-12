package com.softwebdevelopers.ecommerce.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class ECOMSecurityContextHolder {

    private Authentication auth = null;

    public ECOMSecurityContextHolder() {
        auth  = SecurityContextHolder.getContext().getAuthentication();
    }

    public UserDetails getUserDetails() {
        return (UserDetails) auth.getPrincipal();
    }

    public String getUsername() {

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public String getPassword() {
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getPassword();
        } else {
            return principal.toString();
        }
    }

    public Collection<?> getAuthorities() {
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getAuthorities();
        } else {
            return null;
        }
    }
}
