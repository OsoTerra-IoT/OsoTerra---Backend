package com.osoterra.ososense.soilmonitoring.domain.services;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReading;

import java.time.LocalDateTime;
import java.util.List;

public interface SoilMonitoringQueryService {

    List<SoilReading> findReadingsByPlotId(Long plotId);

    List<SoilReading> findReadingsByPlotIdBetween(Long plotId, LocalDateTime start, LocalDateTime end);

    List<CalibrationRecord> findCalibrationRecordsByDeviceId(Long deviceId);
}
