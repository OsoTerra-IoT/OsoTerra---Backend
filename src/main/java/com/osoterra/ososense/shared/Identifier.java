package com.osoterra.ososense.shared;

import java.util.Objects;

/**
 * Base class for typed identifiers wrapping a database-generated {@link Long}. Two
 * identifiers are equal when they are of the same concrete type and wrap the same value,
 * which prevents mixing up identifiers that belong to different aggregates.
 */
public abstract class Identifier implements ValueObject {

    private final Long value;

    protected Identifier(Long value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Identifier that = (Identifier) other;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + value + ")";
    }
}
