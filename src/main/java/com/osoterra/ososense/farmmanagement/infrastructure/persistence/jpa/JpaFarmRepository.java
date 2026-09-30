package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.repositories.FarmRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaFarmRepository implements FarmRepository {

    private final SpringDataFarmJpaRepository springDataRepository;

    JpaFarmRepository(SpringDataFarmJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Farm save(Farm farm) {
        FarmJpaEntity saved = springDataRepository.save(FarmMapper.toEntity(farm));
        return FarmMapper.toDomain(saved);
    }

    @Override
    public Optional<Farm> findById(FarmId id) {
        return springDataRepository.findById(id.value()).map(FarmMapper::toDomain);
    }

    @Override
    public List<Farm> findByOwnerId(Long ownerId) {
        return springDataRepository.findByOwnerId(ownerId).stream().map(FarmMapper::toDomain).toList();
    }
}
