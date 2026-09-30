package com.osoterra.ososense.subscriptionbilling.interfaces.rest.controllers;

import com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources.ErrorResource;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Translates this context's domain errors into coherent HTTP responses.
 */
@RestControllerAdvice(basePackages = "com.osoterra.ososense.subscriptionbilling.interfaces")
class SubscriptionBillingDomainExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ErrorResource> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResource(ex.getMessage()));
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    ResponseEntity<ErrorResource> handleBusinessRuleViolation(BusinessRuleViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResource(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResource> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResource(ex.getMessage()));
    }
}
