package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;
import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrendId;

final class SalinityTrendMapper {

    private SalinityTrendMapper() {
    }

    static SalinityTrend toDomain(SalinityTrendJpaEntity entity) {
        return SalinityTrend.reconstruct(
                new SalinityTrendId(entity.getId()),
                entity.getPlotId(),
                entity.getPeriodStartDate(),
                entity.getPeriodEndDate(),
                entity.getSlopeDsMPerDay(),
                entity.getDirection(),
                entity.getReadingCount(),
                entity.getComputedAt());
    }

    static SalinityTrendJpaEntity toEntity(SalinityTrend trend) {
        Long id = trend.getId() == null ? null : trend.getId().value();
        return new SalinityTrendJpaEntity(
                id, trend.getPlotId(), trend.getPeriodStartDate(), trend.getPeriodEndDate(),
                trend.getSlopeDsMPerDay(), trend.getDirection(), trend.getReadingCount(), trend.getComputedAt());
    }
}
