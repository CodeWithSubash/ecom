package com.softwebdevelopers.ecommerce.exceptions;

public class ECOMException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ECOMException(String message) {
        super(message);
    }

    public ECOMException(String message, Throwable cause) {
        super(message, cause);
    }
}
