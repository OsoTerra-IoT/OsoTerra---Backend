package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.shared.domain.model.ValueObject;

import java.util.Objects;

public record PersonName(String firstName, String lastName) implements ValueObject {

    public PersonName {
        Objects.requireNonNull(firstName, "firstName");
        Objects.requireNonNull(lastName, "lastName");
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new IllegalArgumentException("First name and last name must not be blank");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
