package com.osoterra.ososense.farmmanagement.interfaces.rest.controllers;

import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.services.AssignCropToPlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.AssignCropToPlotCommandService;
import com.osoterra.ososense.farmmanagement.domain.services.DeactivatePlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.DeactivatePlotCommandService;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterPlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterPlotCommandService;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.AssignCropToPlotResource;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.PlotResource;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.PlotResourceAssembler;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.RegisterPlotResource;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plots")
class PlotController {

    private final RegisterPlotCommandService registerPlotCommandService;
    private final AssignCropToPlotCommandService assignCropToPlotCommandService;
    private final DeactivatePlotCommandService deactivatePlotCommandService;
    private final FarmManagementQueryService farmManagementQueryService;
    private final PlotResourceAssembler plotResourceAssembler;

    PlotController(
            RegisterPlotCommandService registerPlotCommandService,
            AssignCropToPlotCommandService assignCropToPlotCommandService,
            DeactivatePlotCommandService deactivatePlotCommandService,
            FarmManagementQueryService farmManagementQueryService,
            PlotResourceAssembler plotResourceAssembler) {
        this.registerPlotCommandService = registerPlotCommandService;
        this.assignCropToPlotCommandService = assignCropToPlotCommandService;
        this.deactivatePlotCommandService = deactivatePlotCommandService;
        this.farmManagementQueryService = farmManagementQueryService;
        this.plotResourceAssembler = plotResourceAssembler;
    }

    @PostMapping
    ResponseEntity<PlotResource> register(@Valid @RequestBody RegisterPlotResource request) {
        Plot plot = registerPlotCommandService.handle(new RegisterPlotCommand(
                request.farmId(), request.name(), request.areaHectares(), request.latitude(), request.longitude()));
        return ResponseEntity.status(HttpStatus.CREATED).body(plotResourceAssembler.toResource(plot));
    }

    @PostMapping("/{id}/crop")
    ResponseEntity<PlotResource> assignCrop(
            @PathVariable Long id, @Valid @RequestBody AssignCropToPlotResource request) {
        Plot plot = assignCropToPlotCommandService.handle(new AssignCropToPlotCommand(id, request.cropId()));
        return ResponseEntity.ok(plotResourceAssembler.toResource(plot));
    }

    @PostMapping("/{id}/deactivation")
    ResponseEntity<PlotResource> deactivate(@PathVariable Long id) {
        Plot plot = deactivatePlotCommandService.handle(new DeactivatePlotCommand(id));
        return ResponseEntity.ok(plotResourceAssembler.toResource(plot));
    }

    @GetMapping("/{id}")
    PlotResource getById(@PathVariable Long id) {
        return farmManagementQueryService
                .findPlotById(new PlotId(id))
                .map(plotResourceAssembler::toResource)
                .orElseThrow(() -> new EntityNotFoundException("Plot not found for id " + id));
    }

    @GetMapping
    List<PlotResource> byFarm(@RequestParam Long farmId) {
        return farmManagementQueryService.findPlotsByFarmId(new FarmId(farmId)).stream()
                .map(plotResourceAssembler::toResource)
                .toList();
    }
}
