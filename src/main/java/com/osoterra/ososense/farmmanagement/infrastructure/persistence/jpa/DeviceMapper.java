package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;

final class DeviceMapper {

    private DeviceMapper() {
    }

    static Device toDomain(DeviceJpaEntity entity) {
        PlotId plotId = entity.getPlotId() == null ? null : new PlotId(entity.getPlotId());
        return Device.reconstruct(
                new DeviceId(entity.getId()),
                plotId,
                entity.getActivationCode(),
                entity.getStatus(),
                entity.getCalibrationFactor(),
                entity.getBatteryLevel(),
                entity.getFirmwareVersion(),
                entity.getReadingIntervalMinutes(),
                entity.getLastSeenAt(),
                entity.getCreatedAt());
    }

    static DeviceJpaEntity toEntity(Device device) {
        Long id = device.getId() == null ? null : device.getId().value();
        Long plotId = device.getPlotId().map(PlotId::value).orElse(null);
        return new DeviceJpaEntity(
                id,
                plotId,
                device.getActivationCode(),
                device.getStatus(),
                device.getCalibrationFactor(),
                device.getBatteryLevel().orElse(null),
                device.getFirmwareVersion().orElse(null),
                device.getReadingIntervalMinutes(),
                device.getLastSeenAt().orElse(null),
                device.getCreatedAt());
    }
}
