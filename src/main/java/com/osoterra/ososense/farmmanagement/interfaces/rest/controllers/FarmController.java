package com.osoterra.ososense.farmmanagement.interfaces.rest.controllers;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterFarmCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterFarmCommandService;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.FarmResource;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.FarmResourceAssembler;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.RegisterFarmResource;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import com.osoterra.ososense.shared.interfaces.rest.CurrentUserId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farms")
class FarmController {

    private final RegisterFarmCommandService registerFarmCommandService;
    private final FarmManagementQueryService farmManagementQueryService;
    private final FarmResourceAssembler farmResourceAssembler;

    FarmController(
            RegisterFarmCommandService registerFarmCommandService,
            FarmManagementQueryService farmManagementQueryService,
            FarmResourceAssembler farmResourceAssembler) {
        this.registerFarmCommandService = registerFarmCommandService;
        this.farmManagementQueryService = farmManagementQueryService;
        this.farmResourceAssembler = farmResourceAssembler;
    }

    @PostMapping
    ResponseEntity<FarmResource> register(
            @Valid @RequestBody RegisterFarmResource request, @CurrentUserId Long ownerId) {
        Farm farm = registerFarmCommandService.handle(new RegisterFarmCommand(
                ownerId, request.name(), request.department(), request.province(), request.district()));
        return ResponseEntity.status(HttpStatus.CREATED).body(farmResourceAssembler.toResource(farm));
    }

    @GetMapping("/mine")
    List<FarmResource> mine(@CurrentUserId Long ownerId) {
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
