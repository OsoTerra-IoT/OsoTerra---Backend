package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterCalibrationRecordResource(
        @NotNull Long deviceId,
        @NotNull BigDecimal labConductivityDsM,
        @NotNull LocalDate samplingDate,
        @NotBlank String laboratoryName,
        @NotNull BigDecimal deviceReadingAtSampling) {
}
