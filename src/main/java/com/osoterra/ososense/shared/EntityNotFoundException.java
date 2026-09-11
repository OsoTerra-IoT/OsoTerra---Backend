package com.osoterra.ososense.shared;

/**
 * Raised when an aggregate or entity is looked up by identity and does not exist.
 */
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
