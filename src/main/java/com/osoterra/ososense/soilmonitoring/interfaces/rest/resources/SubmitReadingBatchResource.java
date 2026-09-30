package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubmitReadingBatchResource(@NotNull Long deviceId, @NotEmpty List<@Valid ReadingPayloadResource> readings) {
}
