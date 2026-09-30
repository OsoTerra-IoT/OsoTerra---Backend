package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.AlertSeverity;
import com.osoterra.ososense.salinityalerting.domain.model.AlertStatus;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;
import com.osoterra.ososense.salinityalerting.domain.repositories.SalinityAlertRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaSalinityAlertRepository implements SalinityAlertRepository {

    private final SpringDataSalinityAlertJpaRepository springDataRepository;

    JpaSalinityAlertRepository(SpringDataSalinityAlertJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public SalinityAlert save(SalinityAlert alert) {
        SalinityAlertJpaEntity saved = springDataRepository.save(SalinityAlertMapper.toEntity(alert));
        return SalinityAlertMapper.toDomain(saved);
    }

    @Override
    public Optional<SalinityAlert> findById(SalinityAlertId id) {
        return springDataRepository.findById(id.value()).map(SalinityAlertMapper::toDomain);
    }

    @Override
    public List<SalinityAlert> findByPlotId(Long plotId) {
        return springDataRepository.findByPlotId(plotId).stream().map(SalinityAlertMapper::toDomain).toList();
    }

    @Override
    public boolean existsByPlotIdAndSeverityAndStatus(Long plotId, AlertSeverity severity, AlertStatus status) {
        return springDataRepository.existsByPlotIdAndSeverityAndStatus(plotId, severity, status);
    }
}
