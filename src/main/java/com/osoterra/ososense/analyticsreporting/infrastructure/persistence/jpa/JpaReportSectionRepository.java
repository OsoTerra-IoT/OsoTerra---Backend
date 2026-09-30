package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;
import com.osoterra.ososense.analyticsreporting.domain.model.ReportSection;
import com.osoterra.ososense.analyticsreporting.domain.repositories.ReportSectionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class JpaReportSectionRepository implements ReportSectionRepository {

    private final SpringDataReportSectionJpaRepository springDataRepository;

    JpaReportSectionRepository(SpringDataReportSectionJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ReportSection save(ReportSection section) {
        ReportSectionJpaEntity saved = springDataRepository.save(ReportSectionMapper.toEntity(section));
        return ReportSectionMapper.toDomain(saved);
    }

    @Override
    public List<ReportSection> findByPlotReportIdOrderByDisplayOrderAsc(PlotReportId plotReportId) {
        return springDataRepository.findByPlotReportIdOrderByDisplayOrderAsc(plotReportId.value()).stream()
                .map(ReportSectionMapper::toDomain)
                .toList();
    }
}
