package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.shared.domain.model.ValueObject;

import java.util.Objects;

/**
 * First and last name of the person who owns an account. Neither part may be blank.
 */
public record PersonName(String firstName, String lastName) implements ValueObject {

    public PersonName {
        Objects.requireNonNull(firstName, "firstName");
        Objects.requireNonNull(lastName, "lastName");
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new IllegalArgumentException("First name and last name must not be blank");
        }
    }

    /**
     * Returns the first and last name separated by a single space, as shown in the apps.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
