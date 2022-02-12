package com.softwebdevelopers.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocesableException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnprocesableException() {
        super();
    }

    public UnprocesableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprocesableException(String message) {
        super(message);
    }

    public UnprocesableException(Throwable cause) {
        super(cause);
    }
}
