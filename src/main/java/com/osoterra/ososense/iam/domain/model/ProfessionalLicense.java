package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.shared.domain.model.ValueObject;

import java.util.Objects;

/**
 * Professional license number of an agronomist advisor. Only advisor accounts carry one,
 * and the number must not be blank.
 */
public record ProfessionalLicense(String number) implements ValueObject {

    public ProfessionalLicense {
        Objects.requireNonNull(number, "number");
        if (number.isBlank()) {
            throw new IllegalArgumentException("Professional license number must not be blank");
        }
    }
}
