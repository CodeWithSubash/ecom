package com.softwebdevelopers.ecommerce.security.resources;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserMapper;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.security.jwt.JwtTokenProvider;
import com.softwebdevelopers.ecommerce.security.model.AuthenticationRequest;
import com.softwebdevelopers.ecommerce.security.model.AuthenticationResponse;
import com.softwebdevelopers.ecommerce.security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUserBL {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UUserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public AuthenticationResponse authentication(AuthenticationRequest dto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = "Bearer " + jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            User user = userRepository.findByUsername(((UserPrincipal) authentication.getPrincipal()).getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

            return new AuthenticationResponse(accessToken, refreshToken, userMapper.toDto(user));
        } catch (DisabledException e) {
            throw new AccessDeniedException(ECOMMessage.EMAIL_NOT_VERIFIED, e);
        } catch (BadCredentialsException e) {
            throw new AccessDeniedException(ECOMMessage.BAD_CREDENTIALS, e);
        } catch (Exception e) {
            System.out.println("Exception while login: ");
            e.printStackTrace();
        }
        return null;
    }
}
