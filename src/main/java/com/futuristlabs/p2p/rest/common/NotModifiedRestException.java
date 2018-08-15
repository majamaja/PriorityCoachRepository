package com.futuristlabs.p2p.rest.common;

import org.springframework.http.HttpStatus;

public class NotModifiedRestException extends RestException {
    public NotModifiedRestException() {
        super(HttpStatus.NOT_MODIFIED);
    }
}
