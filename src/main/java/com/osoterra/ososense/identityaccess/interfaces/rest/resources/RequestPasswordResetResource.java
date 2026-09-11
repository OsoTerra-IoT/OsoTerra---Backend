package com.osoterra.ososense.identityaccess.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestPasswordResetResource(@NotBlank @Email String email) {
}
