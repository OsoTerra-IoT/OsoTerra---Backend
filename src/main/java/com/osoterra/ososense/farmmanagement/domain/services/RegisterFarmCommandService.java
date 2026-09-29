package com.osoterra.ososense.farmmanagement.domain.services;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;

public interface RegisterFarmCommandService {

    Farm handle(RegisterFarmCommand command);
}
