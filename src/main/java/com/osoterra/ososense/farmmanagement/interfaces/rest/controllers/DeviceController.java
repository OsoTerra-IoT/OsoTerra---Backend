package com.osoterra.ososense.farmmanagement.interfaces.rest.controllers;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.services.AttachDeviceToPlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.AttachDeviceToPlotCommandService;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterDeviceCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterDeviceCommandService;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.AttachDeviceToPlotResource;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.DeviceResource;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.DeviceResourceAssembler;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.RegisterDeviceResource;
import com.osoterra.ososense.shared.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/devices")
class DeviceController {

    private final RegisterDeviceCommandService registerDeviceCommandService;
    private final AttachDeviceToPlotCommandService attachDeviceToPlotCommandService;
    private final FarmManagementQueryService farmManagementQueryService;
    private final DeviceResourceAssembler deviceResourceAssembler;

    DeviceController(
            RegisterDeviceCommandService registerDeviceCommandService,
            AttachDeviceToPlotCommandService attachDeviceToPlotCommandService,
            FarmManagementQueryService farmManagementQueryService,
            DeviceResourceAssembler deviceResourceAssembler) {
        this.registerDeviceCommandService = registerDeviceCommandService;
        this.attachDeviceToPlotCommandService = attachDeviceToPlotCommandService;
        this.farmManagementQueryService = farmManagementQueryService;
        this.deviceResourceAssembler = deviceResourceAssembler;
    }

    @PostMapping
    ResponseEntity<DeviceResource> register(@Valid @RequestBody RegisterDeviceResource request) {
        Device device = registerDeviceCommandService.handle(new RegisterDeviceCommand(request.activationCode()));
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceResourceAssembler.toResource(device));
    }

    @PostMapping("/{id}/attachment")
    ResponseEntity<DeviceResource> attachToPlot(
            @PathVariable Long id, @Valid @RequestBody AttachDeviceToPlotResource request) {
        Device device = attachDeviceToPlotCommandService.handle(new AttachDeviceToPlotCommand(id, request.plotId()));
        return ResponseEntity.ok(deviceResourceAssembler.toResource(device));
    }

    @GetMapping("/{id}")
    DeviceResource getById(@PathVariable Long id) {
        return farmManagementQueryService
                .findDeviceById(new DeviceId(id))
                .map(deviceResourceAssembler::toResource)
                .orElseThrow(() -> new EntityNotFoundException("Device not found for id " + id));
    }
}
