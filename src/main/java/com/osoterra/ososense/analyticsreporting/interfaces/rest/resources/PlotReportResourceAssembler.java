package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import org.springframework.stereotype.Component;

@Component
public class PlotReportResourceAssembler {

    public PlotReportResource toResource(PlotReport report) {
        return new PlotReportResource(
                report.getId().value(),
                report.getPlotId(),
                report.getGeneratedBy(),
                report.getPeriodStartDate(),
                report.getPeriodEndDate(),
                report.getGeneratedAt());
    }
}
