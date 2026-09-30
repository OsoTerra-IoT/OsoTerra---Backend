package com.osoterra.ososense.iam.interfaces.rest.controllers;

import com.osoterra.ososense.iam.domain.exceptions.InvalidCredentialsException;
import com.osoterra.ososense.shared.interfaces.rest.ApiErrorResource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Translates this context's own domain errors; everything else falls through to the
 * shared global handler.
 */
@RestControllerAdvice(basePackages = "com.osoterra.ososense.iam.interfaces")
@Order(Ordered.HIGHEST_PRECEDENCE)
class DomainExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    ResponseEntity<ApiErrorResource> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiErrorResource.of(ex.getMessage()));
    }
}
