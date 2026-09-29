package com.osoterra.ososense.soilmonitoring.domain.services;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;

public interface RegisterCalibrationRecordCommandService {

    CalibrationRecord handle(RegisterCalibrationRecordCommand command);
}
