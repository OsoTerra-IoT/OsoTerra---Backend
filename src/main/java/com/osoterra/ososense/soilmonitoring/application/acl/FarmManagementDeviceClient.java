package com.osoterra.ososense.soilmonitoring.application.acl;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.farmmanagement.domain.services.RecordDeviceActivityCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RecordDeviceActivityCommandService;
import com.osoterra.ososense.soilmonitoring.domain.gateways.DeviceActivityRecorder;
import com.osoterra.ososense.soilmonitoring.domain.gateways.DeviceLocationLookup;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Anti-corruption layer between Soil Monitoring and Farm Management's device structure: it
 * resolves the plain plot id and reading interval this context needs and reports the device's
 * readings back, without exposing Farm Management's domain types any further than this class.
 */
@Component
class FarmManagementDeviceClient implements DeviceLocationLookup, DeviceActivityRecorder {

    private final FarmManagementQueryService farmManagementQueryService;
    private final RecordDeviceActivityCommandService recordDeviceActivityCommandService;

    FarmManagementDeviceClient(
            FarmManagementQueryService farmManagementQueryService,
            RecordDeviceActivityCommandService recordDeviceActivityCommandService) {
        this.farmManagementQueryService = farmManagementQueryService;
        this.recordDeviceActivityCommandService = recordDeviceActivityCommandService;
    }

    @Override
    public Optional<Long> findPlotIdForDevice(Long deviceId) {
        return farmManagementQueryService
                .findDeviceById(new DeviceId(deviceId))
                .flatMap(Device::getPlotId)
                .map(PlotId::value);
    }

    @Override
    public Optional<Integer> findReadingIntervalMinutes(Long deviceId) {
        return farmManagementQueryService
                .findDeviceById(new DeviceId(deviceId))
                .map(Device::getReadingIntervalMinutes);
    }

    @Override
    public void recordReading(Long deviceId, LocalDateTime capturedAt) {
        recordDeviceActivityCommandService.handle(new RecordDeviceActivityCommand(deviceId, capturedAt));
    }
}
