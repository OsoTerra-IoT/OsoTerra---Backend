package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.repositories.FarmRepository;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterFarmCommand;
import com.osoterra.ososense.farmmanagement.domain.services.RegisterFarmCommandService;
import org.springframework.stereotype.Service;

@Service
class RegisterFarmCommandServiceImpl implements RegisterFarmCommandService {

    private final FarmRepository farmRepository;

    RegisterFarmCommandServiceImpl(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }

    @Override
    public Farm handle(RegisterFarmCommand command) {
        Farm farm = Farm.register(
                command.ownerId(), command.name(), command.department(), command.province(), command.district());
        return farmRepository.save(farm);
    }
}
