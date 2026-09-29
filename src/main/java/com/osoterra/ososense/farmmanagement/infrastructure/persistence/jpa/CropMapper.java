package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.model.CropId;

final class CropMapper {

    private CropMapper() {
    }

    static Crop toDomain(CropJpaEntity entity) {
        return Crop.reconstruct(
                new CropId(entity.getId()),
                entity.getCommonName(),
                entity.getScientificName(),
                entity.getSalinityThresholdDsM(),
                entity.getSaltToleranceClass(),
                entity.getSourceReference());
    }

    static CropJpaEntity toEntity(Crop crop) {
        Long id = crop.getId() == null ? null : crop.getId().value();
        return new CropJpaEntity(
                id,
                crop.getCommonName(),
                crop.getScientificName().orElse(null),
                crop.getSalinityThresholdDsM(),
                crop.getSaltToleranceClass(),
                crop.getSourceReference().orElse(null));
    }
}
