package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.shared.ValueObject;

import java.util.Objects;

public record ProfessionalLicense(String number) implements ValueObject {

    public ProfessionalLicense {
        Objects.requireNonNull(number, "number");
        if (number.isBlank()) {
            throw new IllegalArgumentException("Professional license number must not be blank");
        }
    }
}
