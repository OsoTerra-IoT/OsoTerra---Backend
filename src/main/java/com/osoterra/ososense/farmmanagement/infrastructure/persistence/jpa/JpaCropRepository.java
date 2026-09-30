package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.model.CropId;
import com.osoterra.ososense.farmmanagement.domain.repositories.CropRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaCropRepository implements CropRepository {

    private final SpringDataCropJpaRepository springDataRepository;

    JpaCropRepository(SpringDataCropJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Crop save(Crop crop) {
        CropJpaEntity saved = springDataRepository.save(CropMapper.toEntity(crop));
        return CropMapper.toDomain(saved);
    }

    @Override
    public Optional<Crop> findById(CropId id) {
        return springDataRepository.findById(id.value()).map(CropMapper::toDomain);
    }

    @Override
    public Optional<Crop> findByCommonName(String commonName) {
        return springDataRepository.findByCommonName(commonName).map(CropMapper::toDomain);
    }

    @Override
    public boolean existsByCommonName(String commonName) {
        return springDataRepository.existsByCommonName(commonName);
    }

    @Override
    public List<Crop> findAll() {
        return springDataRepository.findAll().stream().map(CropMapper::toDomain).toList();
    }
}
