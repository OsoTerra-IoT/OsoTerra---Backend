package com.osoterra.ososense.shared;

/**
 * Raised when an operation would violate an invariant or policy of the domain.
 */
public class BusinessRuleViolationException extends DomainException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
