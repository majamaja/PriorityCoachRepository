package com.futuristlabs.p2p.rest.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(NotModifiedRestException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public void handleNotModified(NotModifiedRestException ex) {

    }

    @ExceptionHandler(RestException.class)
    @ResponseBody
    public HttpErrors handleRestException(HttpServletResponse response, RestException ex) {
        response.setStatus(ex.getStatusCode().value());

        return new HttpErrors(ex.getErrors());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpErrors handleRestException(HttpServletResponse response, Exception ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        return new HttpErrors(ex.getMessage());
    }

}
