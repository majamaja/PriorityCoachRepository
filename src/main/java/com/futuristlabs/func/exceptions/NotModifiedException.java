package com.futuristlabs.func.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_MODIFIED)
public class NotModifiedException extends ApplicationException {
    public NotModifiedException() {
    }

    public NotModifiedException(String message) {
        super(message);
    }

    public NotModifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
