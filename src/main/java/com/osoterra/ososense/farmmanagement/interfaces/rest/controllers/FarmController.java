package com.osoterra.ososense.farmmanagement.interfaces.rest.controllers;

import com.osoterra.ososense.farmmanagement.domain.gateways.AdvisoryAccessLookup;
import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterFarmCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterFarmCommandService;
import com.osoterra.ososense.farmmanagement.domain.services.UpdateFarmCommand;
import com.osoterra.ososense.farmmanagement.domain.services.UpdateFarmCommandService;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.FarmResource;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.FarmResourceAssembler;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.RegisterFarmResource;
import com.osoterra.ososense.shared.EntityNotFoundException;
import com.osoterra.ososense.shared.web.CurrentUserId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farms")
class FarmController {

    private final RegisterFarmCommandService registerFarmCommandService;
    private final FarmManagementQueryService farmManagementQueryService;
    private final FarmResourceAssembler farmResourceAssembler;
    private final AdvisoryAccessLookup advisoryAccessLookup;
    private final UpdateFarmCommandService updateFarmCommandService;

    FarmController(
            RegisterFarmCommandService registerFarmCommandService,
            UpdateFarmCommandService updateFarmCommandService,
            FarmManagementQueryService farmManagementQueryService,
            FarmResourceAssembler farmResourceAssembler,
            AdvisoryAccessLookup advisoryAccessLookup) {
        this.registerFarmCommandService = registerFarmCommandService;
        this.updateFarmCommandService = updateFarmCommandService;
        this.farmManagementQueryService = farmManagementQueryService;
        this.farmResourceAssembler = farmResourceAssembler;
        this.advisoryAccessLookup = advisoryAccessLookup;
    }

    @PostMapping
    ResponseEntity<FarmResource> register(
            @Valid @RequestBody RegisterFarmResource request, @CurrentUserId Long ownerId) {
        Farm farm = registerFarmCommandService.handle(new RegisterFarmCommand(
                ownerId, request.name(), request.department(), request.province(), request.district()));
        return ResponseEntity.status(HttpStatus.CREATED).body(farmResourceAssembler.toResource(farm));
    }

    @PutMapping("/{id}")
    FarmResource update(
            @PathVariable Long id, @Valid @RequestBody RegisterFarmResource request, @CurrentUserId Long userId) {
        Farm farm = updateFarmCommandService.handle(new UpdateFarmCommand(
                id, userId, request.name(), request.department(), request.province(), request.district()));
        return farmResourceAssembler.toResource(farm);
    }

    @GetMapping("/mine")
    List<FarmResource> mine(@CurrentUserId Long ownerId) {
        return farmManagementQueryService.findFarmsByOwnerId(ownerId).stream()
                .map(farmResourceAssembler::toResource)
                .toList();
    }

    /**
     * Farms of a given farmer, readable by the farmer and by advisors linked to them.
     */
    @GetMapping
    List<FarmResource> byOwner(@RequestParam Long ownerId, @CurrentUserId Long userId) {
        if (!ownerId.equals(userId) && !advisoryAccessLookup.isLinked(userId, ownerId)) {
            throw new AccessDeniedException("No active advisory link with farmer " + ownerId);
        }
        return farmManagementQueryService.findFarmsByOwnerId(ownerId).stream()
                .map(farmResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/{id}")
    FarmResource getById(@PathVariable Long id) {
        return farmManagementQueryService
                .findFarmById(new FarmId(id))
                .map(farmResourceAssembler::toResource)
                .orElseThrow(() -> new EntityNotFoundException("Farm not found for id " + id));
    }
}
