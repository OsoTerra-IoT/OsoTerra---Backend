package com.osoterra.ososense.soilmonitoring.application.acl;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.soilmonitoring.domain.gateways.DeviceLocationLookup;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Anti-corruption layer translating a query into Farm Management's device structure
 * into the plain plot id this context needs, without exposing Farm Management's
 * domain types any further than this class.
 */
@Component
class FarmManagementDeviceClient implements DeviceLocationLookup {

    private final FarmManagementQueryService farmManagementQueryService;

    FarmManagementDeviceClient(FarmManagementQueryService farmManagementQueryService) {
        this.farmManagementQueryService = farmManagementQueryService;
    }

    @Override
    public Optional<Long> findPlotIdForDevice(Long deviceId) {
        return farmManagementQueryService
                .findDeviceById(new DeviceId(deviceId))
                .flatMap(Device::getPlotId)
                .map(PlotId::value);
    }
}
