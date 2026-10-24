package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.repositories.DeviceRepository;
import com.osoterra.ososense.farmmanagement.domain.services.RecordDeviceActivityCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RecordDeviceActivityCommandService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class RecordDeviceActivityCommandServiceImpl implements RecordDeviceActivityCommandService {

    private final DeviceRepository deviceRepository;

    RecordDeviceActivityCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device handle(RecordDeviceActivityCommand command) {
        Device device = deviceRepository
                .findById(new DeviceId(command.deviceId()))
                .orElseThrow(() -> new EntityNotFoundException("Device not found for id " + command.deviceId()));
        device.recordReading(command.capturedAt());
        return deviceRepository.save(device);
    }
}
