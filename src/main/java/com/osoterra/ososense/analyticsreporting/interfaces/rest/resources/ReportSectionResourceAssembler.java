package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import com.osoterra.ososense.analyticsreporting.domain.model.ReportSection;
import org.springframework.stereotype.Component;

@Component
public class ReportSectionResourceAssembler {

    public ReportSectionResource toResource(ReportSection section) {
        return new ReportSectionResource(
                section.getId().value(),
                section.getPlotReportId().value(),
                section.getTitle(),
                section.getSectionType(),
                section.getContent(),
                section.getDisplayOrder());
    }
}
