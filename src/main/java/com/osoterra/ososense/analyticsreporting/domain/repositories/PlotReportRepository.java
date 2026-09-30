package com.osoterra.ososense.analyticsreporting.domain.repositories;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;

import java.util.List;
import java.util.Optional;

public interface PlotReportRepository {

    PlotReport save(PlotReport report);

    Optional<PlotReport> findById(PlotReportId id);

    List<PlotReport> findByPlotId(Long plotId);
}
