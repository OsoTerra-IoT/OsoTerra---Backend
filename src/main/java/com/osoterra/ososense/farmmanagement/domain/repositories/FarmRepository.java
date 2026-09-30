package com.osoterra.ososense.farmmanagement.domain.repositories;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;

import java.util.List;
import java.util.Optional;

public interface FarmRepository {

    Farm save(Farm farm);

    Optional<Farm> findById(FarmId id);

    List<Farm> findByOwnerId(Long ownerId);
}
