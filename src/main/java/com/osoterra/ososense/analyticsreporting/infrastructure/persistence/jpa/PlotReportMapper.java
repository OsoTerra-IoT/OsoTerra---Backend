package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;

final class PlotReportMapper {

    private PlotReportMapper() {
    }

    static PlotReport toDomain(PlotReportJpaEntity entity) {
        return PlotReport.reconstruct(
                new PlotReportId(entity.getId()),
                entity.getPlotId(),
                entity.getGeneratedBy(),
                entity.getPeriodStartDate(),
                entity.getPeriodEndDate(),
                entity.getGeneratedAt());
    }

    static PlotReportJpaEntity toEntity(PlotReport report) {
        Long id = report.getId() == null ? null : report.getId().value();
        return new PlotReportJpaEntity(
                id, report.getPlotId(), report.getGeneratedBy(), report.getPeriodStartDate(),
                report.getPeriodEndDate(), report.getGeneratedAt());
    }
}
