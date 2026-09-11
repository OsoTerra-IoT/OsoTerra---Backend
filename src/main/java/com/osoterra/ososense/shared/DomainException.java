package com.osoterra.ososense.shared;

/**
 * Base type for exceptions raised by the domain model. Application and interface layers
 * translate these into command results or HTTP responses instead of exposing them as-is.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
