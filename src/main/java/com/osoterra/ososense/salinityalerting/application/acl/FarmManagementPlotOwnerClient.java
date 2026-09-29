package com.osoterra.ososense.salinityalerting.application.acl;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.salinityalerting.domain.gateways.PlotOwnerLookup;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Anti-corruption layer resolving the IAM user account that owns a plot, by following
 * plot -&gt; farm -&gt; owner through Farm Management.
 */
@Component
class FarmManagementPlotOwnerClient implements PlotOwnerLookup {

    private final FarmManagementQueryService farmManagementQueryService;

    FarmManagementPlotOwnerClient(FarmManagementQueryService farmManagementQueryService) {
        this.farmManagementQueryService = farmManagementQueryService;
    }

    @Override
    public Optional<Long> findOwnerIdForPlot(Long plotId) {
        return farmManagementQueryService
                .findPlotById(new PlotId(plotId))
                .map(Plot::getFarmId)
                .flatMap(farmManagementQueryService::findFarmById)
                .map(Farm::getOwnerId);
    }
}
