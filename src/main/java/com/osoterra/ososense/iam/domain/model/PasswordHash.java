package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.shared.ValueObject;

import java.util.Objects;

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
