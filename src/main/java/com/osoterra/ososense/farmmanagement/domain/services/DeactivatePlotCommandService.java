package com.osoterra.ososense.farmmanagement.domain.services;

import com.osoterra.ososense.farmmanagement.domain.model.Plot;

public interface DeactivatePlotCommandService {

    Plot handle(DeactivatePlotCommand command);
}
