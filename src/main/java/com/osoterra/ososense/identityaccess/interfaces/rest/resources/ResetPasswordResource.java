package com.osoterra.ososense.identityaccess.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordResource(@NotBlank String token, @NotBlank @Size(min = 8) String newPassword) {
}
