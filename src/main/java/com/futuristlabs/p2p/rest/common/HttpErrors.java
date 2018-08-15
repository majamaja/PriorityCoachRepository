package com.futuristlabs.p2p.rest.common;

public class HttpErrors {
    private final String[] errors;

    public HttpErrors(String error) {
        this.errors = new String[]{ error};
    }

    public HttpErrors(String[] errors) {
        this.errors = errors;
    }

    public String[] getErrors() {
        return errors;
    }
}
