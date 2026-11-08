package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.repositories.DeviceRepository;
import com.osoterra.ososense.farmmanagement.domain.services.ApplyDeviceCalibrationCommand;
import com.osoterra.ososense.farmmanagement.domain.services.ApplyDeviceCalibrationCommandService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class ApplyDeviceCalibrationCommandServiceImpl implements ApplyDeviceCalibrationCommandService {

    private final DeviceRepository deviceRepository;

    ApplyDeviceCalibrationCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device handle(ApplyDeviceCalibrationCommand command) {
        Device device = deviceRepository
                .findById(new DeviceId(command.deviceId()))
                .orElseThrow(() -> new EntityNotFoundException("Device not found for id " + command.deviceId()));
        device.applyCalibrationFactor(command.calibrationFactor());
        return deviceRepository.save(device);
    }
}
