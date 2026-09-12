package com.osoterra.ososense.iam.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Client-facing representation of a {@code UserAccount}. Deliberately excludes the
 * password hash and any other sensitive credential data.
 */
public record UserAccountResource(
        Long id,
        String email,
        String firstName,
        String lastName,
        String role,
        String professionalLicenseNumber,
        boolean active,
        LocalDateTime createdAt) {
}
