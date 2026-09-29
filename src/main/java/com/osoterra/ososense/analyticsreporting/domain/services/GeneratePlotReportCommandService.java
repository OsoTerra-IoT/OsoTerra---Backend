package com.osoterra.ososense.analyticsreporting.domain.services;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;

public interface GeneratePlotReportCommandService {

    PlotReport handle(GeneratePlotReportCommand command);
}
