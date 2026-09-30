package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;
import org.springframework.stereotype.Component;

@Component
public class SalinityTrendResourceAssembler {

    public SalinityTrendResource toResource(SalinityTrend trend) {
        return new SalinityTrendResource(
                trend.getId().value(),
                trend.getPlotId(),
                trend.getPeriodStartDate(),
                trend.getPeriodEndDate(),
                trend.getSlopeDsMPerDay(),
                trend.getDirection().name(),
                trend.getReadingCount(),
                trend.getComputedAt());
    }
}
