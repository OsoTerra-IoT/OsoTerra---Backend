package com.osoterra.ososense.analyticsreporting.domain.repositories;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;
import com.osoterra.ososense.analyticsreporting.domain.model.ReportSection;

import java.util.List;

public interface ReportSectionRepository {

    ReportSection save(ReportSection section);

    List<ReportSection> findByPlotReportIdOrderByDisplayOrderAsc(PlotReportId plotReportId);
}
