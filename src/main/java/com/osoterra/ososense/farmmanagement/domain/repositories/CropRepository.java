package com.osoterra.ososense.farmmanagement.domain.repositories;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.model.CropId;

import java.util.List;
import java.util.Optional;

public interface CropRepository {

    Crop save(Crop crop);

    Optional<Crop> findById(CropId id);

    Optional<Crop> findByCommonName(String commonName);

    boolean existsByCommonName(String commonName);

    List<Crop> findAll();
}
