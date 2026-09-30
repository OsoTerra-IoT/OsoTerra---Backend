package com.osoterra.ososense.shared.interfaces.rest;

import java.util.Map;

/**
 * Error body returned by every bounded context: a message for people and, for validation
 * errors, the message of each rejected field.
 */
public record ApiErrorResource(String message, Map<String, String> fieldErrors) {

    public static ApiErrorResource of(String message) {
        return new ApiErrorResource(message, Map.of());
    }
}
