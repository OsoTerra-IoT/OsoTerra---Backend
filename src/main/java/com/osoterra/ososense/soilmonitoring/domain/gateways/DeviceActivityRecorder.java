package com.osoterra.ososense.soilmonitoring.domain.gateways;

import java.time.LocalDateTime;

/**
 * Tells the context that owns the device that one of its readings arrived, so it can keep
 * the device's last-seen time and online status. Implemented by an anti-corruption layer
 * client into Farm Management.
 */
public interface DeviceActivityRecorder {

    void recordReading(Long deviceId, LocalDateTime capturedAt);
}
