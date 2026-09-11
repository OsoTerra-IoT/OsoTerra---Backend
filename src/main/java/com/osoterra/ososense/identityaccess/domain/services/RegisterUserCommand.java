package com.osoterra.ososense.identityaccess.domain.services;

import com.osoterra.ososense.identityaccess.domain.model.UserRole;

public record RegisterUserCommand(
        String email,
        String rawPassword,
        String firstName,
        String lastName,
        UserRole role,
        String professionalLicenseNumber) {
}
