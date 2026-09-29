package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;
import com.osoterra.ososense.analyticsreporting.domain.model.ReportSection;
import com.osoterra.ososense.analyticsreporting.domain.model.ReportSectionId;

final class ReportSectionMapper {

    private ReportSectionMapper() {
    }

    static ReportSection toDomain(ReportSectionJpaEntity entity) {
        return ReportSection.reconstruct(
                new ReportSectionId(entity.getId()),
                new PlotReportId(entity.getPlotReportId()),
                entity.getTitle(),
                entity.getSectionType(),
                entity.getContent(),
                entity.getDisplayOrder());
    }

    static ReportSectionJpaEntity toEntity(ReportSection section) {
        Long id = section.getId() == null ? null : section.getId().value();
        return new ReportSectionJpaEntity(
                id, section.getPlotReportId().value(), section.getTitle(), section.getSectionType(),
                section.getContent(), section.getDisplayOrder());
    }
}
