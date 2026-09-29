package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.repositories.DeviceRepository;
import com.osoterra.ososense.farmmanagement.domain.repositories.PlotRepository;
import com.osoterra.ososense.farmmanagement.domain.services.AttachDeviceToPlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.AttachDeviceToPlotCommandService;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class AttachDeviceToPlotCommandServiceImpl implements AttachDeviceToPlotCommandService {

    private final DeviceRepository deviceRepository;
    private final PlotRepository plotRepository;

    AttachDeviceToPlotCommandServiceImpl(DeviceRepository deviceRepository, PlotRepository plotRepository) {
        this.deviceRepository = deviceRepository;
        this.plotRepository = plotRepository;
    }

    @Override
    public Device handle(AttachDeviceToPlotCommand command) {
        Device device = deviceRepository
                .findById(new DeviceId(command.deviceId()))
                .orElseThrow(() -> new EntityNotFoundException("Device not found for id " + command.deviceId()));
        Plot plot = plotRepository
                .findById(new PlotId(command.plotId()))
                .orElseThrow(() -> new EntityNotFoundException("Plot not found for id " + command.plotId()));
        device.attachToPlot(plot.getId());
        return deviceRepository.save(device);
    }
}
