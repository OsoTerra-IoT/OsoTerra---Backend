package com.osoterra.ososense.farmmanagement.domain.services;

import com.osoterra.ososense.farmmanagement.domain.model.Plot;

public interface RegisterPlotCommandService {

    Plot handle(RegisterPlotCommand command);
}
