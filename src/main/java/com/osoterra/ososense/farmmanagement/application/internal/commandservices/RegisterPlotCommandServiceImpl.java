package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.gateways.PlotQuotaLookup;
import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.GeoCoordinates;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.repositories.FarmRepository;
import com.osoterra.ososense.farmmanagement.domain.repositories.PlotRepository;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterPlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterPlotCommandService;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registers a new plot under an existing farm. The owner's active plots may not exceed the
 * limit of their active subscription plan; an owner without an active subscription has no
 * limit (current behaviour, kept on purpose: no default plan limit is invented here).
 */
@Service
@Transactional
class RegisterPlotCommandServiceImpl implements RegisterPlotCommandService {

    private final FarmRepository farmRepository;
    private final PlotRepository plotRepository;

    private final PlotQuotaLookup plotQuotaLookup;

    RegisterPlotCommandServiceImpl(
            FarmRepository farmRepository, PlotRepository plotRepository, PlotQuotaLookup plotQuotaLookup) {
        this.farmRepository = farmRepository;
        this.plotRepository = plotRepository;
        this.plotQuotaLookup = plotQuotaLookup;
    }

    @Override
    public Plot handle(RegisterPlotCommand command) {
        Farm farm = farmRepository
                .findById(new FarmId(command.farmId()))
                .orElseThrow(() -> new EntityNotFoundException("Farm not found for id " + command.farmId()));
        checkPlotQuota(farm.getOwnerId());
        Plot plot = Plot.register(
                farm.getId(),
                command.name(),
                command.areaHectares(),
                new GeoCoordinates(command.latitude(), command.longitude()));
        return plotRepository.save(plot);
    }

    private void checkPlotQuota(Long ownerId) {
        plotQuotaLookup.findPlotLimitForOwner(ownerId).ifPresent(limit -> {
            long active = farmRepository.findByOwnerId(ownerId).stream()
                    .flatMap(ownerFarm -> plotRepository.findByFarmId(ownerFarm.getId()).stream())
                    .filter(Plot::isActive)
                    .count();
            if (active >= limit) {
                throw new BusinessRuleViolationException(
                        "The subscription plan allows " + limit + " active plots and you already have " + active);
            }
        });
    }
}
