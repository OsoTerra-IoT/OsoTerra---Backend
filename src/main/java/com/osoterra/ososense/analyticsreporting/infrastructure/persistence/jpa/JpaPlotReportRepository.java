package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;
import com.osoterra.ososense.analyticsreporting.domain.repositories.PlotReportRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaPlotReportRepository implements PlotReportRepository {

    private final SpringDataPlotReportJpaRepository springDataRepository;

    JpaPlotReportRepository(SpringDataPlotReportJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public PlotReport save(PlotReport report) {
        PlotReportJpaEntity saved = springDataRepository.save(PlotReportMapper.toEntity(report));
        return PlotReportMapper.toDomain(saved);
    }

    @Override
    public Optional<PlotReport> findById(PlotReportId id) {
        return springDataRepository.findById(id.value()).map(PlotReportMapper::toDomain);
    }

    @Override
    public List<PlotReport> findByPlotId(Long plotId) {
        return springDataRepository.findByPlotId(plotId).stream().map(PlotReportMapper::toDomain).toList();
    }
}
