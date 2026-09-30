package com.osoterra.ososense.iam.domain.services;

import com.osoterra.ososense.iam.domain.model.UserRole;

/**
 * The role and professional license are only used the first time a given Google
 * identity signs in, to create the account; on subsequent sign-ins they are ignored.
 */
public record AuthenticateWithGoogleCommand(String idToken, UserRole role, String professionalLicenseNumber) {
}
