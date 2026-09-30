package com.osoterra.ososense.farmmanagement.domain.services;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;

public interface UpdateFarmCommandService {
    Farm handle(UpdateFarmCommand command);
}
