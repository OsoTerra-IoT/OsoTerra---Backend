package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.GeoCoordinates;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.repositories.FarmRepository;
import com.osoterra.ososense.farmmanagement.domain.repositories.PlotRepository;
import com.osoterra.ososense.farmmanagement.domain.services.UpdatePlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.UpdatePlotCommandService;
import com.osoterra.ososense.shared.EntityNotFoundException;
import com.osoterra.ososense.shared.ForbiddenOperationException;
import org.springframework.stereotype.Service;

/**
 * Changes a plot's name, area and location. Only the owner of its farm may do it.
 */
@Service
class UpdatePlotCommandServiceImpl implements UpdatePlotCommandService {

    private final PlotRepository plotRepository;
    private final FarmRepository farmRepository;

    UpdatePlotCommandServiceImpl(PlotRepository plotRepository, FarmRepository farmRepository) {
        this.plotRepository = plotRepository;
        this.farmRepository = farmRepository;
    }

    @Override
    public Plot handle(UpdatePlotCommand command) {
        Plot plot = plotRepository
                .findById(new PlotId(command.plotId()))
                .orElseThrow(() -> new EntityNotFoundException("Plot not found for id " + command.plotId()));
        boolean requestedByOwner = farmRepository.findById(plot.getFarmId())
                .map(Farm::getOwnerId)
                .filter(command.requestedBy()::equals)
                .isPresent();
        if (!requestedByOwner) {
            throw new ForbiddenOperationException("Only the farm owner can change plot " + command.plotId());
        }
        plot.updateDetails(
                command.name(), command.areaHectares(), new GeoCoordinates(command.latitude(), command.longitude()));
        return plotRepository.save(plot);
    }
}
