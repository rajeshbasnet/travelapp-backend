package com.example.travel.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, String>> handleAllException(Exception exception) {
        log.info("Handling exception ...");


        if (exception instanceof EmailAlreadyExistsException ex) {
            return handleServiceOrDataLayerException(ex, HttpStatus.BAD_REQUEST);
        }


        if (exception instanceof MethodArgumentNotValidException ex) {
            return handleBeanValidationException(ex, HttpStatus.BAD_REQUEST);
        }

        if (exception instanceof BadCredentialsException ex) {
            return handleServiceOrDataLayerException(ex, HttpStatus.UNAUTHORIZED);
        }

        if (exception instanceof UsernameNotFoundException ex) {
            return handleServiceOrDataLayerException(ex, HttpStatus.BAD_REQUEST);
        }

        if (exception instanceof MalformedJwtException ex) {
            return handleServiceOrDataLayerException(ex, HttpStatus.BAD_REQUEST);
        }

        if (exception instanceof NotAuthenticatedException ex) {
            return handleServiceOrDataLayerException(ex, HttpStatus.FORBIDDEN);
        }

        if (exception instanceof NotAuthorizedException ex) {
            return handleServiceOrDataLayerException(ex, HttpStatus.UNAUTHORIZED);
        }

        if (exception instanceof ExpiredJwtException ex) {
            return handleExceptionWithCustomMessage(ex, "You session has expired. Please, login again.", HttpStatus.FORBIDDEN);
        }

        if (exception instanceof AccessDeniedException) {
            AccessDeniedException ex = (AccessDeniedException) exception;
            return handleServiceOrDataLayerException(ex, HttpStatus.FORBIDDEN);
        }

        return handleInternalServerError(exception);
    }

    private ResponseEntity<Map<String, String>> handleServiceOrDataLayerException(Exception exception, HttpStatus status) {
        log.warn("{}, {}", exception.getClass(), exception.getMessage());

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<Map<String, String>> handleBeanValidationException(MethodArgumentNotValidException exception, HttpStatus status) {
        log.warn("{}, {}", exception.getClass(), exception.getMessage());

        return ResponseEntity.status(status).body(
                exception.getFieldErrors()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        FieldError::getField, Objects.requireNonNull(FieldError::getDefaultMessage),
                                        (existing, replacement) -> replacement
                                )
                        )
        );
    }

    private ResponseEntity<Map<String, String>> handleInternalServerError(Exception exception) {
        log.error("Exception occurred", exception);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    private ResponseEntity<Map<String, String>> handleExceptionWithCustomMessage(Exception exception, String message, HttpStatus status) {
        log.warn("{}, {}", exception.getClass(), exception.getMessage());

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}
