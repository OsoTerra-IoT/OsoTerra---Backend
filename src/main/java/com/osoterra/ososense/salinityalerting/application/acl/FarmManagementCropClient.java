package com.osoterra.ososense.salinityalerting.application.acl;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.salinityalerting.domain.gateways.CropThresholdLookup;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Anti-corruption layer resolving a plot's assigned crop and its salinity tolerance
 * threshold from Farm Management, which owns both the assignment and the catalog.
 */
@Component
class FarmManagementCropClient implements CropThresholdLookup {

    private final FarmManagementQueryService farmManagementQueryService;

    FarmManagementCropClient(FarmManagementQueryService farmManagementQueryService) {
        this.farmManagementQueryService = farmManagementQueryService;
    }

    @Override
    public Optional<BigDecimal> findThresholdForPlot(Long plotId) {
        return farmManagementQueryService
                .findPlotById(new PlotId(plotId))
                .flatMap(Plot::getCropId)
                .flatMap(farmManagementQueryService::findCropById)
                .map(Crop::getSalinityThresholdDsM);
    }
}
