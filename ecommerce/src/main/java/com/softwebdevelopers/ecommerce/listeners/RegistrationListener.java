package com.softwebdevelopers.ecommerce.listeners;
import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.ECOMConstants.EmailTemplates;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.listeners.registration.OnRegistrationCompleteEvent;
import com.softwebdevelopers.ecommerce.services.IUUserService;
import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RegistrationListener  implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Value("${email.app-url}")
    private String APP_URL;

    @Autowired
    IUUserService userServices;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
        User user = event.getUser();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        System.out.println("--APP URL: " + event.getAppUrl());
        Context context = new Context();
        context.setVariable("appUrl", event.getAppUrl());
        context.setVariable("token", event.getToken());
        context.setVariable("message", event.getType()
                .equalsIgnoreCase(ECOMConstants.NEW_EMAIL)
                ? ECOMMessage.EMAIL_BODY_REGISTRATION_TOKEN
                : event.getType()
                .equalsIgnoreCase(ECOMConstants.FORGOT_PASSWORD_TOKEN_EMAIL)
                ? ECOMMessage.EMAIL_BODY_FORGOT_PASSWORD_TOKEN
                : ECOMMessage.EMAIL_BODY_RESEND_REGISTRATION_TOKEN);

        String html = templateEngine.process(event.getType()
                .equalsIgnoreCase(ECOMConstants.FORGOT_PASSWORD_TOKEN_EMAIL)
                ? EmailTemplates.FORGOT_PASSWORD_TOKEN_TEMPLATE : EmailTemplates.USER_ACTIVATION_TEMPLATE, context);

        String recipentAddress = user.getEmail();
        helper.setSubject(event.getType()
                .equalsIgnoreCase(ECOMConstants.NEW_EMAIL)
                ? ECOMMessage.EMAIL_SUBJECT_REGISTRATION_TOKEN
                : event.getType()
                .equalsIgnoreCase(ECOMConstants.FORGOT_PASSWORD_TOKEN_EMAIL)
                ? ECOMMessage.EMAIL_SUBJECT_FORGOT_PASSWORD_TOKEN
                : ECOMMessage.EMAIL_SUBJECT_RESEND_REGISTRATION_TOKEN);

        helper.setFrom(ECOMConstants.EMAIL_SENDER_ID);
        helper.setTo(recipentAddress);
        helper.setText(html, true);

        log.info("Sending Mail to: " + recipentAddress);
        mailSender.send(message);
        log.info(("Mail Sent"));

    }
}
