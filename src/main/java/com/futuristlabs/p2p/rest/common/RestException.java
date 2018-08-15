package com.futuristlabs.p2p.rest.common;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String[] errors;

    public RestException(HttpStatus statusCode, String... errors) {
        this.statusCode = statusCode;
        this.errors = errors;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String[] getErrors() {
        return errors;
    }
}
