package com.osoterra.ososense.farmmanagement.application.internal;

import com.osoterra.ososense.farmmanagement.domain.gateways.AdvisoryAccessLookup;
import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import com.osoterra.ososense.shared.domain.exceptions.ForbiddenOperationException;
import com.osoterra.ososense.shared.domain.services.PlotAccessPolicy;
import org.springframework.stereotype.Component;

/**
 * Resolves access from the farm's owner and the owner's accepted advisory links.
 */
@Component
class FarmManagementPlotAccessPolicy implements PlotAccessPolicy {

    private final FarmManagementQueryService farmManagementQueryService;
    private final AdvisoryAccessLookup advisoryAccessLookup;

    FarmManagementPlotAccessPolicy(
            FarmManagementQueryService farmManagementQueryService, AdvisoryAccessLookup advisoryAccessLookup) {
        this.farmManagementQueryService = farmManagementQueryService;
        this.advisoryAccessLookup = advisoryAccessLookup;
    }

    @Override
    public void checkFarmRead(Long userId, Long farmId) {
        Farm farm = farm(farmId);
        if (!canRead(userId, farm)) {
            throw new ForbiddenOperationException("No access to farm " + farmId);
        }
    }

    @Override
    public void checkFarmWrite(Long userId, Long farmId) {
        if (!farm(farmId).getOwnerId().equals(userId)) {
            throw new ForbiddenOperationException("Only the owner can change farm " + farmId);
        }
    }

    @Override
    public void checkPlotRead(Long userId, Long plotId) {
        if (!canRead(userId, farm(plot(plotId).getFarmId().value()))) {
            throw new ForbiddenOperationException("No access to plot " + plotId);
        }
    }

    @Override
    public void checkPlotWrite(Long userId, Long plotId) {
        if (!farm(plot(plotId).getFarmId().value()).getOwnerId().equals(userId)) {
            throw new ForbiddenOperationException("Only the farm owner can change plot " + plotId);
        }
    }

    @Override
    public void checkDeviceRead(Long userId, Long deviceId) {
        Device device = farmManagementQueryService
                .findDeviceById(new DeviceId(deviceId))
                .orElseThrow(() -> new EntityNotFoundException("Device not found for id " + deviceId));
        device.getPlotId().ifPresent(plotId -> checkPlotRead(userId, plotId.value()));
    }

    private boolean canRead(Long userId, Farm farm) {
        return farm.getOwnerId().equals(userId) || advisoryAccessLookup.isLinked(userId, farm.getOwnerId());
    }

    private Farm farm(Long farmId) {
        return farmManagementQueryService
                .findFarmById(new FarmId(farmId))
                .orElseThrow(() -> new EntityNotFoundException("Farm not found for id " + farmId));
    }

    private Plot plot(Long plotId) {
        return farmManagementQueryService
                .findPlotById(new PlotId(plotId))
                .orElseThrow(() -> new EntityNotFoundException("Plot not found for id " + plotId));
    }
}
