package com.osoterra.ososense.soilmonitoring.domain.gateways;

import java.math.BigDecimal;

/**
 * Hands a newly derived calibration factor to the context that owns the device, so the
 * sensor keeps the factor its future readings are compensated with. Implemented by an
 * anti-corruption layer client into Farm Management.
 */
public interface DeviceCalibrationUpdater {

    void applyCalibrationFactor(Long deviceId, BigDecimal calibrationFactor);
}
