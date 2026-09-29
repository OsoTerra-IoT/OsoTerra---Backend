package com.osoterra.ososense.soilmonitoring.domain.repositories;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecordId;

import java.util.List;
import java.util.Optional;

public interface CalibrationRecordRepository {

    CalibrationRecord save(CalibrationRecord record);

    Optional<CalibrationRecord> findById(CalibrationRecordId id);

    List<CalibrationRecord> findByDeviceId(Long deviceId);
}
