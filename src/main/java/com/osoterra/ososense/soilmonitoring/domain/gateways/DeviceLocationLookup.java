package com.osoterra.ososense.soilmonitoring.domain.gateways;

import java.util.Optional;

/**
 * Resolves which plot a device is currently installed in. Implemented by an
 * anti-corruption layer client into Farm Management, which owns that structural data.
 */
public interface DeviceLocationLookup {

    Optional<Long> findPlotIdForDevice(Long deviceId);
}
