package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.repositories.DeviceRepository;
import com.osoterra.ososense.farmmanagement.domain.services.MarkDeviceOfflineCommand;
import com.osoterra.ososense.farmmanagement.domain.services.MarkDeviceOfflineCommandService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class MarkDeviceOfflineCommandServiceImpl implements MarkDeviceOfflineCommandService {

    private final DeviceRepository deviceRepository;

    MarkDeviceOfflineCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device handle(MarkDeviceOfflineCommand command) {
        Device device = deviceRepository
                .findById(new DeviceId(command.deviceId()))
                .orElseThrow(() -> new EntityNotFoundException("Device not found for id " + command.deviceId()));
        device.markOffline();
        return deviceRepository.save(device);
    }
}
