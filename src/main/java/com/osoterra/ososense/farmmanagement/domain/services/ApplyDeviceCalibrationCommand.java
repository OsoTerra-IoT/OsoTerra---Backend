package com.osoterra.ososense.farmmanagement.domain.services;

import java.math.BigDecimal;

public record ApplyDeviceCalibrationCommand(Long deviceId, BigDecimal calibrationFactor) {
}
