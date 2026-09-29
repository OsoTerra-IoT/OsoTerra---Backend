package com.osoterra.ososense.soilmonitoring.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterCalibrationRecordCommand(
        Long deviceId, BigDecimal labConductivityDsM, LocalDate samplingDate, String laboratoryName,
        BigDecimal deviceReadingAtSampling) {
}
