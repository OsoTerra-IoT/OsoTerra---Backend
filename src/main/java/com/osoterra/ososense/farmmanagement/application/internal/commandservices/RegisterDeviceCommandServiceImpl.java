package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.repositories.DeviceRepository;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterDeviceCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterDeviceCommandService;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;
import org.springframework.stereotype.Service;

@Service
class RegisterDeviceCommandServiceImpl implements RegisterDeviceCommandService {

    private final DeviceRepository deviceRepository;

    RegisterDeviceCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device handle(RegisterDeviceCommand command) {
        if (deviceRepository.existsByActivationCode(command.activationCode())) {
            throw new BusinessRuleViolationException("A device already exists for this activation code");
        }
        Device device = Device.registerWithActivationCode(command.activationCode());
        return deviceRepository.save(device);
    }
}
