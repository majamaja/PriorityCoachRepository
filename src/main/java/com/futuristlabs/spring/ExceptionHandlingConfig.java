package com.futuristlabs.spring;

import com.futuristlabs.func.exceptions.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandlingConfig extends ResponseEntityExceptionHandler {
    @Data
    @AllArgsConstructor
    private static class ErrorDescription {
        private final String error;
    }

    private HttpStatus resolveAnnotatedResponseStatus(Exception exception) {
        ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return annotation.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private ErrorDescription resolveExceptionMessage(Exception exception) {
        return new ErrorDescription(exception.getMessage());
    }

    @ExceptionHandler({ApplicationException.class})
    protected ResponseEntity<Object> handleApplicationExceptions(ApplicationException ex, WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpStatus status = resolveAnnotatedResponseStatus(ex);
        ErrorDescription message = resolveExceptionMessage(ex);
        return new ResponseEntity<>(message, headers, status);
    }
}
