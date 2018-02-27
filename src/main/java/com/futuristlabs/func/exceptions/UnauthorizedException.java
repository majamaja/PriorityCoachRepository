package com.futuristlabs.func.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException() {
        super("Unauthorized");
    }
}
