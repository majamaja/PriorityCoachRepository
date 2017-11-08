package com.futuristlabs.spring;

import com.futuristlabs.func.exceptions.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ExceptionHandlingConfig extends ResponseEntityExceptionHandler {
    @Data
    @AllArgsConstructor
    private static class ErrorDescription {
        private final String error;
    }

    @Data
    @AllArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    @Data
    @AllArgsConstructor
    private static class ValidationErrors {
        private final List<ValidationError> errors;

        static ValidationErrors from(final BindingResult br) {
            final List<ValidationError> errors = br.getFieldErrors().stream()
                    .map(field -> new ValidationError(field.getField(), field.getDefaultMessage()))
                    .collect(toList());

            return new ValidationErrors(errors);
        }
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

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(ValidationErrors.from(ex.getBindingResult()), headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(ValidationErrors.from(ex.getBindingResult()), headers, HttpStatus.BAD_REQUEST);
    }
}
