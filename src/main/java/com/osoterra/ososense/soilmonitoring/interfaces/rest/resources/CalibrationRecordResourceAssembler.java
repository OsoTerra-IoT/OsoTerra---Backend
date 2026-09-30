package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import org.springframework.stereotype.Component;

@Component
public class CalibrationRecordResourceAssembler {

    public CalibrationRecordResource toResource(CalibrationRecord record) {
        return new CalibrationRecordResource(
                record.getId().value(),
                record.getDeviceId(),
                record.getLabConductivityDsM(),
                record.getSamplingDate(),
                record.getLaboratoryName(),
                record.getDeviceReadingAtSampling(),
                record.getResultingFactor(),
                record.getRegisteredAt());
    }
}
