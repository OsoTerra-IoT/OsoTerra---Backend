package com.osoterra.ososense.analyticsreporting.application.acl;

import com.osoterra.ososense.analyticsreporting.domain.gateways.PlotStructureLookup;
import com.osoterra.ososense.analyticsreporting.domain.gateways.PlotSummary;
import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Anti-corruption layer resolving a plot's structural summary from Farm Management,
 * translated into this context's own {@link PlotSummary} shape.
 */
@Component
class FarmManagementPlotStructureClient implements PlotStructureLookup {

    private final FarmManagementQueryService farmManagementQueryService;

    FarmManagementPlotStructureClient(FarmManagementQueryService farmManagementQueryService) {
        this.farmManagementQueryService = farmManagementQueryService;
    }

    @Override
    public Optional<PlotSummary> findPlotSummary(Long plotId) {
        return farmManagementQueryService.findPlotById(new PlotId(plotId)).map(this::toSummary);
    }

    private PlotSummary toSummary(Plot plot) {
        String cropName = plot.getCropId()
                .flatMap(farmManagementQueryService::findCropById)
                .map(Crop::getCommonName)
                .orElse(null);
        return new PlotSummary(
                plot.getId().value(), plot.getFarmId().value(), plot.getName(), plot.getAreaHectares(), cropName);
    }
}
