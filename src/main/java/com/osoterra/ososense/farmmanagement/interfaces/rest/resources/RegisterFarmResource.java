package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record RegisterFarmResource(
        @NotBlank String name, @NotBlank String department, @NotBlank String province, @NotBlank String district) {
}
