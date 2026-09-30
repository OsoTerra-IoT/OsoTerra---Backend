package com.osoterra.ososense.shared;

/**
 * Raised when the requesting user is authenticated but not allowed to act on the target
 * aggregate, for example when changing a farm they do not own.
 */
public class ForbiddenOperationException extends DomainException {

    public ForbiddenOperationException(String message) {
        super(message);
    }
}
