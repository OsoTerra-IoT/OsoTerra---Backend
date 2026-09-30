package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;
import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrendId;
import com.osoterra.ososense.analyticsreporting.domain.repositories.SalinityTrendRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaSalinityTrendRepository implements SalinityTrendRepository {

    private final SpringDataSalinityTrendJpaRepository springDataRepository;

    JpaSalinityTrendRepository(SpringDataSalinityTrendJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public SalinityTrend save(SalinityTrend trend) {
        SalinityTrendJpaEntity saved = springDataRepository.save(SalinityTrendMapper.toEntity(trend));
        return SalinityTrendMapper.toDomain(saved);
    }

    @Override
    public Optional<SalinityTrend> findById(SalinityTrendId id) {
        return springDataRepository.findById(id.value()).map(SalinityTrendMapper::toDomain);
    }

    @Override
    public List<SalinityTrend> findByPlotId(Long plotId) {
        return springDataRepository.findByPlotId(plotId).stream().map(SalinityTrendMapper::toDomain).toList();
    }
}
