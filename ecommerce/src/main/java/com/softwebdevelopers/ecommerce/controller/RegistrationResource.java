package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.business.URegistrationBL;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.URegistrationDto;
import com.softwebdevelopers.ecommerce.dto.UUserDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordConflictException;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + RegistrationResource.AUTHENTICATED_PATH)
public class RegistrationResource {
    public static final String AUTHENTICATED_PATH = "/secured";

    @Autowired
    private URegistrationBL registrationService;

    @Autowired
    private UUserRepository userRepository;

    @PostMapping("/registration")
    public ResponseEntity<?> userRegistration(@RequestBody URegistrationDto registrationDto) {
        UUserDto user = registrationService.registerUser(registrationDto);
        return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/registrationConfirm/{token}")
    public ResponseEntity<Message> confirmRegistration(@PathVariable("token") String token) {
        registrationService.verifyToken(token);
        return new ResponseEntity<Message>(ECOMConstants.PROCEED_TO_LOGIN, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/validate/{type}")
    public boolean validateEmail(@Validated @RequestBody URegistrationDto registrationDto, @PathVariable String type) {

        if (type.equalsIgnoreCase(ECOMConstants.EMAIL_TEXT)) {
            return userRepository.existsByEmail(registrationDto.getEmail());
        } else if (type.equalsIgnoreCase(ECOMConstants.USERNAME_TEXT)) {
            return userRepository.existsByUsername(registrationDto.getUsername());
        }
        return true;
    }

    @GetMapping("/resendRegistrationToken")
    public ResponseEntity<Message> resendRegistrationToken(@RequestParam("email") String email) throws MessagingException {

        try {
            registrationService.resendVerificationToken(email);
            return new ResponseEntity<Message>(ECOMConstants.NEW_MAIL_SENT, new HttpHeaders(), HttpStatus.OK);

        } catch (RecordConflictException e) {
            return new ResponseEntity<Message>(Message.error(e.getLocalizedMessage()), new HttpHeaders(),
                    HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/generatePasswordToken")
    public ResponseEntity<Message> getForgetPasswordToken(@RequestParam("email") String email) throws MessagingException {

        try {
            registrationService.resendForgetPasswordToken(email);
            return new ResponseEntity<Message>(ECOMConstants.NEW_MAIL_SENT, new HttpHeaders(), HttpStatus.OK);

        } catch (RecordConflictException e) {
            return new ResponseEntity<Message>(Message.error(e.getLocalizedMessage()), new HttpHeaders(),
                    HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/requestForgotPassword/{token}/changePassword")
    public ResponseEntity<Message> changePassword(
            @PathVariable(required = true) String token,
            @Validated @RequestBody URegistrationDto registrationDto) {

        if (Preconditions.checkNull(registrationDto.getPassword())
                || Preconditions.checkNull(registrationDto.getEmail())
                || !registrationDto.getPassword().equals(registrationDto.getRePassword())) {

            return ResponseEntity.badRequest().body(ECOMConstants.PASSWORD_REPASSWORD_NOT_MATCHED);
        }

        Message message = registrationService.changePassword(registrationDto, token);
        return new ResponseEntity<Message>(message, new HttpHeaders(), HttpStatus.OK);
    }
}
