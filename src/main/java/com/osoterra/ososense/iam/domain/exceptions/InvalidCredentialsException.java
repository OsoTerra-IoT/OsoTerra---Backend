package com.osoterra.ososense.iam.domain.exceptions;

import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;

/**
 * Raised when authentication fails. Carries a generic message on purpose: it must not
 * reveal whether the email or the password was the one that did not match.
 */
public class InvalidCredentialsException extends BusinessRuleViolationException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
