package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.shared.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email address of a user account. It must have a local part, an at sign and a domain
 * with a dot, and it is stored in lower case so that two spellings of the same address
 * are treated as one account.
 */
public record EmailAddress(String value) implements ValueObject {

    private static final Pattern FORMAT = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public EmailAddress {
        Objects.requireNonNull(value, "value");
        if (!FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email address: " + value);
        }
        value = value.toLowerCase();
    }
}
