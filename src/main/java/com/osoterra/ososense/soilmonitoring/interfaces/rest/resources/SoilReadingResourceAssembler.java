package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatchId;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReading;
import org.springframework.stereotype.Component;

@Component
public class SoilReadingResourceAssembler {

    public SoilReadingResource toResource(SoilReading reading) {
        return new SoilReadingResource(
                reading.getId().value(),
                reading.getDeviceId(),
                reading.getPlotId(),
                reading.getReadingBatchId().map(ReadingBatchId::value).orElse(null),
                reading.getRawConductivityDsM(),
                reading.getCompensatedConductivityDsM(),
                reading.getCompensationFactor(),
                reading.getMoisturePercentage(),
                reading.getTemperatureCelsius(),
                reading.getCapturedAt(),
                reading.getStoredAt());
    }
}
