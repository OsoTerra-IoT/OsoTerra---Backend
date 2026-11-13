package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.shared.domain.model.ValueObject;

import java.util.Objects;

/**
 * Hashed password of an account together with the algorithm that produced it. The raw
 * password is never stored, and {@link #toString()} omits the hash so it cannot leak into
 * logs.
 */
public record PasswordHash(String value, String algorithm) implements ValueObject {

    public PasswordHash {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(algorithm, "algorithm");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Password hash value must not be blank");
        }
    }

    @Override
    public String toString() {
        return "PasswordHash{algorithm=" + algorithm + "}";
    }
}
