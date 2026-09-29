package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CalibrationRecordResource(
        Long id, Long deviceId, BigDecimal labConductivityDsM, LocalDate samplingDate, String laboratoryName,
        BigDecimal deviceReadingAtSampling, BigDecimal resultingFactor, LocalDateTime registeredAt) {
}
