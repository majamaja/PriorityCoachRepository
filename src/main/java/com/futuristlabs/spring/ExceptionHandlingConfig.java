package com.futuristlabs.spring;

import com.fasterxml.jackson.core.JsonParseException;
import com.futuristlabs.func.exceptions.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ExceptionHandlingConfig extends ResponseEntityExceptionHandler {
    private enum Errors {
        INVALID_JSON(HttpStatus.BAD_REQUEST),
        NOT_FOUND(HttpStatus.NOT_FOUND),
        CONSTRAINT_VIOLATION(HttpStatus.CONFLICT),
        DUPLICATE_DATA(HttpStatus.CONFLICT);

        private HttpStatus status;

        Errors(HttpStatus status) {
            this.status = status;
        }

        private ResponseEntity<Object> response(HttpHeaders headers) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(new ErrorDescription(name()), headers, status);
        }
    }

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

    @ExceptionHandler({ ApplicationException.class })
    protected ResponseEntity<Object> handleApplicationExceptions(ApplicationException ex, WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpStatus status = resolveAnnotatedResponseStatus(ex);
        ErrorDescription message = resolveExceptionMessage(ex);
        return new ResponseEntity<>(message, headers, status);
    }

    @ExceptionHandler({ EmptyResultDataAccessException.class, NoSuchElementException.class })
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        return Errors.NOT_FOUND.response(new HttpHeaders());
    }

    @ExceptionHandler({ DuplicateKeyException.class })
    protected ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        return Errors.DUPLICATE_DATA.response(new HttpHeaders());
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        return Errors.CONSTRAINT_VIOLATION.response(new HttpHeaders());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(ValidationErrors.from(ex.getBindingResult()), headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(ValidationErrors.from(ex.getBindingResult()), headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex.getCause() instanceof JsonParseException) {
            return Errors.INVALID_JSON.response(headers);
        } else {
            return super.handleExceptionInternal(ex, body, headers, status, request);
        }
    }
}
