package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;

final class FarmMapper {

    private FarmMapper() {
    }

    static Farm toDomain(FarmJpaEntity entity) {
        return Farm.reconstruct(
                new FarmId(entity.getId()),
                entity.getOwnerId(),
                entity.getName(),
                entity.getDepartment(),
                entity.getProvince(),
                entity.getDistrict(),
                entity.getCreatedAt());
    }

    static FarmJpaEntity toEntity(Farm farm) {
        Long id = farm.getId() == null ? null : farm.getId().value();
        return new FarmJpaEntity(
                id,
                farm.getOwnerId(),
                farm.getName(),
                farm.getDepartment(),
                farm.getProvince(),
                farm.getDistrict(),
                farm.getCreatedAt());
    }
}
