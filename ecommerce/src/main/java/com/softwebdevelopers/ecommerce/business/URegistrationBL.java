package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.URegistrationDto;
import com.softwebdevelopers.ecommerce.listeners.registration.OnRegistrationCompleteEvent;
import com.softwebdevelopers.ecommerce.exceptions.RecordConflictException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.EUserType;
import com.softwebdevelopers.ecommerce.models.user.UResetPasswordToken;
import com.softwebdevelopers.ecommerce.models.user.UserRetailer;
import com.softwebdevelopers.ecommerce.models.user.UserVerificationToken;
import com.softwebdevelopers.ecommerce.models.modelmapper.RegistrationMapper;
import com.softwebdevelopers.ecommerce.services.IUUserService;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.dto.UUserDto;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserMapper;
import com.softwebdevelopers.ecommerce.exceptions.BadRequestException;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class URegistrationBL {

    @Value("${email.app-url}")
    private String APP_URL;

    @Autowired
    private IUUserService service;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public UUserDto registerUser(URegistrationDto registrationDto) {

        registrationDto.setUserType(EUserType.RETAILER.name());

        User user = registrationMapper.toEntity(registrationDto);

        UserRetailer userKyc = new UserRetailer().toBuilder()
                .businessName(registrationDto.getName())
                .email(registrationDto.getEmail())
                .user(user)
                .deletedDate(LocalDateTime.now())
                .status(true)
                .build();
        user.setUserRetailer(userKyc);
        User entity = service.registerUserAccount(user, registrationDto.getRole());

        UserVerificationToken verificationToken = service.createVerificationToken(
                new UserVerificationToken().toBuilder()
                        .user(entity)
                        .build());

        // Email Publisher
        applicationEventPublisher.publishEvent(
                new OnRegistrationCompleteEvent(entity, verificationToken.getToken(), ECOMConstants.NEW_EMAIL, APP_URL));

        return mapper.toDto(entity);
    }

    public UUserDto verifyToken(String token) {

        UserVerificationToken verificationToken = service.findByVerificationToken(token);

        if (Preconditions.checkNull(verificationToken)) {
            throw new BadRequestException(ECOMConstants.INVALID_TOKEN);
        }

        if (verificationToken.getUser().isEmailVerified()) {
            throw new BadRequestException(ECOMConstants.ALREADY_VERIFIED);
        }

        if (verificationToken.getExpiryDate() <= ECOMUtilities.getUnixTime()) {
            throw new BadRequestException(ECOMConstants.TOKEN_EXPIRED);
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);
        user.setEmailVerified(true);
        user = service.updateRegisteredUser(user);

        return mapper.toDto(user);
    }

    public void resendVerificationToken(String email) throws MessagingException {

        try {
            User user = service.getUserByEmail(email);

            if (Preconditions.checkNotNull(user)
                    && !user.isEnabled()) {

                UserVerificationToken verificationToken = service.createVerificationToken(
                        new UserVerificationToken().toBuilder()
                                .user(user)
                                .build());

                if (Preconditions.checkNotNull(verificationToken)) {

                    /* EXPIRED THE OLD VERIFICATION LINKS IF ANY AVAILABLE */
                    service.updateVerificationTokenEnableFlag(verificationToken.getId(), verificationToken.getUser().getId());

                    // Email Publisher
                    applicationEventPublisher.publishEvent(
                            new OnRegistrationCompleteEvent(user, verificationToken.getToken(), ECOMConstants.RESEND_EMAIL, APP_URL));

                } else {
                    log.error("Error while requesting for verification token having Email: [{}]. Please try again.", email);
                    throw new RecordConflictException("Error while requesting for verification token having Email: ["
                            + email + "]. Please try again.");
                }

            } else {

                log.error("User is already verified while requesting for verification token having Email: [{}].", email);
                throw new RecordConflictException("User is already verified while requesting for verification token having Email: ["
                        + email + "].");
            }
        } catch (RecordNotFoundException e) {

            log.error("User does not exists for requesting verification token having Email: [{}].", email);
            throw new RecordConflictException("User does not exists for requesting verification token having Email: ["
                    + email + "].");
        }
    }

    public void resendForgetPasswordToken(String email) throws MessagingException {

        try {
            User user = service.getUserByEmail(email);

            if (Preconditions.checkNotNull(user)) {

                UResetPasswordToken passwordToken = service.createPasswordToken(
                        new UResetPasswordToken().toBuilder()
                                .user(user)
                                .build());

                if (Preconditions.checkNotNull(passwordToken)) {

                    /* EXPIRED THE OLD PASSWORD LINKS IF ANY AVAILABLE */
                    service.updatePasswordEnableFlag(passwordToken.getId(), passwordToken.getUser().getId());

                    // Email Publisher
                    applicationEventPublisher.publishEvent(
                            new OnRegistrationCompleteEvent(user, passwordToken.getToken(), ECOMConstants.FORGOT_PASSWORD_TOKEN_EMAIL, APP_URL));

                } else {
                    log.error("Error while requesting for forgot password token having Email: [{}]. Please try again.", email);
                    throw new RecordConflictException("Error while requesting for forgot password token having Email: ["
                            + email + "]. Please try again.");
                }

            } else {

                log.error("User is not available while requesting for forgot password token having Email: [{}].", email);
                throw new RecordConflictException("User is not available while requesting for forgot password token having Email: ["
                        + email + "].");
            }
        } catch (RecordNotFoundException e) {

            log.error("User does not exists for requesting forgot password token having Email: [{}].", email);
            throw new RecordConflictException("User does not exists for requesting forgot password token having Email: ["
                    + email + "].");
        }
    }

    public Message changePassword(URegistrationDto dto, String token) {

        UResetPasswordToken passwordToken = service.findByPasswordToken(token);

        if (Preconditions.checkNotNull(passwordToken)
                && passwordToken.getUser().getEmail().equalsIgnoreCase(dto.getEmail())) {

            if (!passwordToken.getUser().isEnabled()) {
                throw new BadRequestException("User can't change the password as the user is not verified.");
            }

            User entity = passwordToken.getUser();

            entity.setPassword(dto.getPassword());
            entity = service.changePassword(entity);

            log.info("The user with user Id: [{}] update successfully. The password changed successfully.", entity.getId());
            return Message.info("The password change successfully.");
        } else {

            log.error("The provide email [{}] does not matched with the token provided.", dto.getEmail());
            throw new RecordConflictException("The provide email [" + dto.getEmail() + "] does not matched with the token provided.");
        }
    }
}
