package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.GeoCoordinates;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.repositories.FarmRepository;
import com.osoterra.ososense.farmmanagement.domain.repositories.PlotRepository;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterPlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterPlotCommandService;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Registers a new plot under an existing farm. Cross-checking the owner's plot quota
 * against Subscription and Billing is intentionally out of scope until that bounded
 * context exists.
 */
@Service
class RegisterPlotCommandServiceImpl implements RegisterPlotCommandService {

    private final FarmRepository farmRepository;
    private final PlotRepository plotRepository;

    RegisterPlotCommandServiceImpl(FarmRepository farmRepository, PlotRepository plotRepository) {
        this.farmRepository = farmRepository;
        this.plotRepository = plotRepository;
    }

    @Override
    public Plot handle(RegisterPlotCommand command) {
        Farm farm = farmRepository
                .findById(new FarmId(command.farmId()))
                .orElseThrow(() -> new EntityNotFoundException("Farm not found for id " + command.farmId()));
        Plot plot = Plot.register(
                farm.getId(),
                command.name(),
                command.areaHectares(),
                new GeoCoordinates(command.latitude(), command.longitude()));
        return plotRepository.save(plot);
    }
}
