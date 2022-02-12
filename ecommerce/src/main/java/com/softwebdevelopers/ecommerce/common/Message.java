package com.softwebdevelopers.ecommerce.common;

import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;

import java.io.Serializable;

public class Message  implements Serializable {

    private static final long serialVersionUID = 1L;
    private EMessageType status;
    private String message;

    public Message() {

    }

    public Message(EMessageType status, String message) {
        this.status = status;
        this.message = message;
    }

    public EMessageType getStatus() {
        return status;
    }

    public void setStatus(EMessageType status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Message success(String message) {
        return new Message(EMessageType.SUCCESS, message);
    }

    public static Message info(String message) {
        return new Message(EMessageType.INFO, message);
    }

    public static Message error(String message) {
        return new Message(EMessageType.ERROR, message);
    }

    public static Message warn(String message) {
        return new Message(EMessageType.WARN, message);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + ECOMUtilities.getJSONString(this, false) + "]";
    }

}
