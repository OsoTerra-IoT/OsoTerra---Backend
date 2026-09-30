package com.osoterra.ososense.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * {@code role} and {@code professionalLicenseNumber} are only used the first time this
 * Google identity signs in, to create the account.
 */
public record GoogleSignInResource(
        @NotBlank String idToken,
        @NotBlank @Pattern(regexp = "FARMER|ADVISOR") String role,
        String professionalLicenseNumber) {
}
