package com.osoterra.ososense.soilmonitoring.application.internal.queryservices;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReading;
import com.osoterra.ososense.soilmonitoring.domain.repositories.CalibrationRecordRepository;
import com.osoterra.ososense.soilmonitoring.domain.repositories.SoilReadingRepository;
import com.osoterra.ososense.soilmonitoring.domain.services.SoilMonitoringQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
class SoilMonitoringQueryServiceImpl implements SoilMonitoringQueryService {

    private final SoilReadingRepository soilReadingRepository;
    private final CalibrationRecordRepository calibrationRecordRepository;

    SoilMonitoringQueryServiceImpl(
            SoilReadingRepository soilReadingRepository, CalibrationRecordRepository calibrationRecordRepository) {
        this.soilReadingRepository = soilReadingRepository;
        this.calibrationRecordRepository = calibrationRecordRepository;
    }

    @Override
    public List<SoilReading> findReadingsByPlotId(Long plotId) {
        return soilReadingRepository.findByPlotIdOrderByCapturedAtDesc(plotId);
    }

    @Override
    public List<SoilReading> findReadingsByPlotIdBetween(Long plotId, LocalDateTime start, LocalDateTime end) {
        return soilReadingRepository.findByPlotIdAndCapturedAtBetween(plotId, start, end);
    }

    @Override
    public List<CalibrationRecord> findCalibrationRecordsByDeviceId(Long deviceId) {
        return calibrationRecordRepository.findByDeviceId(deviceId);
    }
}
