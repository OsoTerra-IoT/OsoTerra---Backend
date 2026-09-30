package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatch;
import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatchId;
import com.osoterra.ososense.soilmonitoring.domain.repositories.ReadingBatchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JpaReadingBatchRepository implements ReadingBatchRepository {

    private final SpringDataReadingBatchJpaRepository springDataRepository;

    JpaReadingBatchRepository(SpringDataReadingBatchJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ReadingBatch save(ReadingBatch batch) {
        ReadingBatchJpaEntity saved = springDataRepository.save(ReadingBatchMapper.toEntity(batch));
        return ReadingBatchMapper.toDomain(saved);
    }

    @Override
    public Optional<ReadingBatch> findById(ReadingBatchId id) {
        return springDataRepository.findById(id.value()).map(ReadingBatchMapper::toDomain);
    }
}
