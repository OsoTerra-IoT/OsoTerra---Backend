package com.osoterra.ososense.analyticsreporting.domain.services;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;
import com.osoterra.ososense.analyticsreporting.domain.model.ReportSection;
import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;

import java.util.List;
import java.util.Optional;

public interface AnalyticsReportingQueryService {

    List<SalinityTrend> findTrendsByPlotId(Long plotId);

    Optional<PlotReport> findReportById(PlotReportId id);

    List<PlotReport> findReportsByPlotId(Long plotId);

    List<ReportSection> findSectionsByReportId(PlotReportId reportId);
}
