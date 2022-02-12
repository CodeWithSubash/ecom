package com.softwebdevelopers.ecommerce.listeners.thankyou;

import com.softwebdevelopers.ecommerce.dto.EmailDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnMailSendingEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String appUrl;
    private EmailDto emailDto;

    public OnMailSendingEvent(EmailDto _emailDto, String _appUrl) {
        super(_emailDto);
        this.emailDto = _emailDto;
        this.appUrl = _appUrl;
    }
}
