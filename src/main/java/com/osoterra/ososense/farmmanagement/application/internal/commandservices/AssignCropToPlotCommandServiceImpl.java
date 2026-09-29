package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.model.CropId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.repositories.CropRepository;
import com.osoterra.ososense.farmmanagement.domain.repositories.PlotRepository;
import com.osoterra.ososense.farmmanagement.domain.services.AssignCropToPlotCommand;
import com.osoterra.ososense.farmmanagement.domain.services.AssignCropToPlotCommandService;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class AssignCropToPlotCommandServiceImpl implements AssignCropToPlotCommandService {

    private final PlotRepository plotRepository;
    private final CropRepository cropRepository;

    AssignCropToPlotCommandServiceImpl(PlotRepository plotRepository, CropRepository cropRepository) {
        this.plotRepository = plotRepository;
        this.cropRepository = cropRepository;
    }

    @Override
    public Plot handle(AssignCropToPlotCommand command) {
        Plot plot = plotRepository
                .findById(new PlotId(command.plotId()))
                .orElseThrow(() -> new EntityNotFoundException("Plot not found for id " + command.plotId()));
        Crop crop = cropRepository
                .findById(new CropId(command.cropId()))
                .orElseThrow(() -> new EntityNotFoundException("Crop not found for id " + command.cropId()));
        plot.assignCrop(crop.getId());
        return plotRepository.save(plot);
    }
}
