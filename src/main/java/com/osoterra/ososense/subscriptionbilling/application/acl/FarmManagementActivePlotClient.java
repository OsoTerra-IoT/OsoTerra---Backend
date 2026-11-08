package com.osoterra.ososense.subscriptionbilling.application.acl;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.subscriptionbilling.domain.gateways.ActivePlotCounter;
import org.springframework.stereotype.Component;

/**
 * Anti-corruption layer counting an owner's active plots through Farm Management's query
 * service, without exposing its domain types beyond this class.
 */
@Component
class FarmManagementActivePlotClient implements ActivePlotCounter {

    private final FarmManagementQueryService farmManagementQueryService;

    FarmManagementActivePlotClient(FarmManagementQueryService farmManagementQueryService) {
        this.farmManagementQueryService = farmManagementQueryService;
    }

    @Override
    public int countActivePlotsOfOwner(Long ownerId) {
        int active = 0;
        for (Farm farm : farmManagementQueryService.findFarmsByOwnerId(ownerId)) {
            active += (int) farmManagementQueryService.findPlotsByFarmId(farm.getId())
                    .stream().filter(Plot::isActive).count();
        }
        return active;
    }
}
