package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import org.springframework.stereotype.Component;

@Component
public class DeviceResourceAssembler {

    public DeviceResource toResource(Device device) {
        return new DeviceResource(
                device.getId().value(),
                device.getPlotId().map(PlotId::value).orElse(null),
                device.getActivationCode(),
                device.getStatus().name(),
                device.getCalibrationFactor(),
                device.getBatteryLevel().orElse(null),
                device.getFirmwareVersion().orElse(null),
                device.getReadingIntervalMinutes(),
                device.getLastSeenAt().orElse(null),
                device.getCreatedAt());
    }
}
