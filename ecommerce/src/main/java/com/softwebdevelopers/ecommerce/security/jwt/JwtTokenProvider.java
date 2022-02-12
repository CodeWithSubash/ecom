package com.softwebdevelopers.ecommerce.security.jwt;

import com.softwebdevelopers.ecommerce.security.model.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.refreshToken.secretKey}")
    private String refreshTokenSecretKey;

    @Value("${jwt.accessToken.secretKey}")
    private String accessTokenSecretKey;

    @Value("${jwt.refreshToken.expiresIn}")
    private Long refreshTokenExpireInMs;

    @Value("${jwt.accessToken.expiresIn}")
    private Long accessTokenExpireInMs;

    public String generateAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpireInMs);
        log.info("Access token expiry date {} of user {} ", expiryDate, userPrincipal.getUsername());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, accessTokenSecretKey)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpireInMs);
        log.info("Refresh token expiry date {} of user {} ", expiryDate, userPrincipal.getUsername());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecretKey)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(accessTokenSecretKey)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateAccessToken(String accessToken)
            throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedOperationException, IllegalArgumentException {
        try {
            Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(accessToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public boolean validateRefreshToken(String refreshToken)
            throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedOperationException, IllegalArgumentException {
        Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(refreshToken);
        return true;
    }
}
