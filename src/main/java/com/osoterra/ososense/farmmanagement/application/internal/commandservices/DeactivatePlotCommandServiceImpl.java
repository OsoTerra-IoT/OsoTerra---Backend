package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.repositories.PlotRepository;
import com.osoterra.ososense.farmmanagement.domain.services.DeactivatePlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.DeactivatePlotCommandService;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class DeactivatePlotCommandServiceImpl implements DeactivatePlotCommandService {

    private final PlotRepository plotRepository;

    DeactivatePlotCommandServiceImpl(PlotRepository plotRepository) {
        this.plotRepository = plotRepository;
    }

    @Override
    public Plot handle(DeactivatePlotCommand command) {
        Plot plot = plotRepository
                .findById(new PlotId(command.plotId()))
                .orElseThrow(() -> new EntityNotFoundException("Plot not found for id " + command.plotId()));
        plot.deactivate();
        return plotRepository.save(plot);
    }
}
