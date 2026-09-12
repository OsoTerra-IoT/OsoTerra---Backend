package com.osoterra.ososense.iam.domain.services;

import com.osoterra.ososense.iam.domain.model.UserRole;

public record RegisterUserCommand(
        String email,
        String rawPassword,
        String firstName,
        String lastName,
        UserRole role,
        String professionalLicenseNumber) {
}
