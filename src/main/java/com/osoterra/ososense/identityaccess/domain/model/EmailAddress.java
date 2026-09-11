package com.osoterra.ososense.identityaccess.domain.model;

import com.osoterra.ososense.shared.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

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
