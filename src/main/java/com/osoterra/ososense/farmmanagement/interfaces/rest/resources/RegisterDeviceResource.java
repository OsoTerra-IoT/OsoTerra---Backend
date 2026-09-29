package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record RegisterDeviceResource(@NotBlank String activationCode) {
}
