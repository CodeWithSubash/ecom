package com.softwebdevelopers.ecommerce.listeners.registration;

import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String appUrl;
    private String token;
    private String type;
    private User user;

    public OnRegistrationCompleteEvent(User user, String token, String type, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.token = token;
        this.type = type;
    }
}
