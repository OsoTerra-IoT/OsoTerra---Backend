package com.osoterra.ososense.salinityalerting.interfaces.rest.resources;

import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import org.springframework.stereotype.Component;

@Component
public class SalinityAlertResourceAssembler {

    public SalinityAlertResource toResource(SalinityAlert alert) {
        return new SalinityAlertResource(
                alert.getId().value(),
                alert.getPlotId(),
                alert.getSoilReadingId(),
                alert.getObservedConductivityDsM(),
                alert.getAppliedThresholdDsM(),
                alert.getExcessRatio(),
                alert.getSeverity().name(),
                alert.getStatus().name(),
                alert.getAcknowledgedBy().orElse(null),
                alert.getAcknowledgedAt().orElse(null),
                alert.getGeneratedAt());
    }
}
