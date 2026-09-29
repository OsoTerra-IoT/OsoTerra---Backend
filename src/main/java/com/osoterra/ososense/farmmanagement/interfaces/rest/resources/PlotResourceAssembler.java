package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import com.osoterra.ososense.farmmanagement.domain.model.CropId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import org.springframework.stereotype.Component;

@Component
public class PlotResourceAssembler {

    public PlotResource toResource(Plot plot) {
        return new PlotResource(
                plot.getId().value(),
                plot.getFarmId().value(),
                plot.getCropId().map(CropId::value).orElse(null),
                plot.getName(),
                plot.getAreaHectares(),
                plot.getCoordinates().latitude(),
                plot.getCoordinates().longitude(),
                plot.isActive(),
                plot.getCreatedAt());
    }
}
