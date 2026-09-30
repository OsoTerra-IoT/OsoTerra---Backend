package com.osoterra.ososense.farmmanagement.interfaces.rest.controllers;

import com.osoterra.ososense.farmmanagement.domain.model.CropId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.CropResource;
import com.osoterra.ososense.farmmanagement.interfaces.rest.resources.CropResourceAssembler;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-only access to the crop salinity tolerance catalog. Crops are preloaded by
 * {@code CropCatalogSeeder}; this context does not expose a way to create or edit them.
 */
@RestController
@RequestMapping("/api/v1/crops")
class CropController {

    private final FarmManagementQueryService farmManagementQueryService;
    private final CropResourceAssembler cropResourceAssembler;

    CropController(FarmManagementQueryService farmManagementQueryService, CropResourceAssembler cropResourceAssembler) {
        this.farmManagementQueryService = farmManagementQueryService;
        this.cropResourceAssembler = cropResourceAssembler;
    }

    @GetMapping
    List<CropResource> all() {
        return farmManagementQueryService.findAllCrops().stream().map(cropResourceAssembler::toResource).toList();
    }

    @GetMapping("/{id}")
    CropResource getById(@PathVariable Long id) {
        return farmManagementQueryService
                .findCropById(new CropId(id))
                .map(cropResourceAssembler::toResource)
                .orElseThrow(() -> new EntityNotFoundException("Crop not found for id " + id));
    }
}
