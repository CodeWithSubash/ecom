package com.softwebdevelopers.ecommerce.listeners;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.ECOMConstants.EmailTemplates;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.EmailDto;
import com.softwebdevelopers.ecommerce.listeners.thankyou.OnMailSendingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ThankYouMailListener implements ApplicationListener<OnMailSendingEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(OnMailSendingEvent event) {
        try {
            this.sendPhaseMail(event);
        } catch (MessagingException e) {
            log.error("Email sending failed.", e);
        }
    }

    private void sendPhaseMail(OnMailSendingEvent event) throws MessagingException {

        EmailDto mailRequest = event.getEmailDto();

//		List<String> recipients = mailRequest.getRecipientEmails().stream().map(item -> item.getRecipientEmail())
//				.collect(Collectors.toList());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariable("appurl", event.getAppUrl());
        context.setVariable("message", mailRequest.getMessage());
        context.setVariable("password", mailRequest.getPassword());

        String html = templateEngine.process(EmailTemplates.THANK_YOU_EMAIL_TEMPLATE, context);

        InternetAddress[] toAddress = new InternetAddress[mailRequest.getRecipientEmails().size()];

        for(int i = 0; i < mailRequest.getRecipientEmails().size(); i++) {
            toAddress[i] = new InternetAddress(mailRequest.getRecipientEmails().get(i));
        }

        helper.setTo(toAddress);
        if (toAddress.length == 1) {
            helper.setCc(toAddress);
        } else {
            helper.setBcc(toAddress);
        }
        helper.setFrom(ECOMConstants.EMAIL_SENDER_ID);
        helper.setSubject(Preconditions.checkNotNull(mailRequest.getSubject()) && mailRequest.getSubject().trim().equalsIgnoreCase("")
                ? mailRequest.getSubject()
                : "Email from ecommerce.");
        helper.setText(html, true);

        log.info("Sending Email...");
        mailSender.send(helper.getMimeMessage());
        log.info(("Email Sent"));
    }
}
