package com.osoterra.ososense.farmmanagement.application.internal.commandservices;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.repositories.FarmRepository;
import com.osoterra.ososense.farmmanagement.domain.services.UpdateFarmCommand;
import com.osoterra.ososense.farmmanagement.domain.services.UpdateFarmCommandService;
import com.osoterra.ososense.shared.EntityNotFoundException;
import com.osoterra.ososense.shared.ForbiddenOperationException;
import org.springframework.stereotype.Service;

/**
 * Changes a farm's name and location. Only its owner may do it.
 */
@Service
class UpdateFarmCommandServiceImpl implements UpdateFarmCommandService {

    private final FarmRepository farmRepository;

    UpdateFarmCommandServiceImpl(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }

    @Override
    public Farm handle(UpdateFarmCommand command) {
        Farm farm = farmRepository
                .findById(new FarmId(command.farmId()))
                .orElseThrow(() -> new EntityNotFoundException("Farm not found for id " + command.farmId()));
        if (!farm.getOwnerId().equals(command.requestedBy())) {
            throw new ForbiddenOperationException("Only the owner can change farm " + command.farmId());
        }
        farm.updateDetails(command.name(), command.department(), command.province(), command.district());
        return farmRepository.save(farm);
    }
}
