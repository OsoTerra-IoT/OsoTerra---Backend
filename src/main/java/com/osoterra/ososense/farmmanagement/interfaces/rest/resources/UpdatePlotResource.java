package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdatePlotResource(
        @NotBlank String name,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal areaHectares,
        @NotNull BigDecimal latitude,
        @NotNull BigDecimal longitude) {
}
