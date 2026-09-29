package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record AttachDeviceToPlotResource(@NotNull Long plotId) {
}
