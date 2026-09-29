package com.osoterra.ososense.farmmanagement.domain.services;

import com.osoterra.ososense.farmmanagement.domain.model.Plot;

public interface AssignCropToPlotCommandService {

    Plot handle(AssignCropToPlotCommand command);
}
