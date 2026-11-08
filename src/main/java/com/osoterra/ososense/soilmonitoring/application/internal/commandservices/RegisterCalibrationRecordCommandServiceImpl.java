package com.osoterra.ososense.soilmonitoring.application.internal.commandservices;

import com.osoterra.ososense.soilmonitoring.domain.gateways.DeviceCalibrationUpdater;
import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import com.osoterra.ososense.soilmonitoring.domain.repositories.CalibrationRecordRepository;
import com.osoterra.ososense.soilmonitoring.domain.services.RegisterCalibrationRecordCommand;
import com.osoterra.ososense.soilmonitoring.domain.services.RegisterCalibrationRecordCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class RegisterCalibrationRecordCommandServiceImpl implements RegisterCalibrationRecordCommandService {

    private final CalibrationRecordRepository calibrationRecordRepository;

    private final DeviceCalibrationUpdater deviceCalibrationUpdater;

    RegisterCalibrationRecordCommandServiceImpl(
            CalibrationRecordRepository calibrationRecordRepository, DeviceCalibrationUpdater deviceCalibrationUpdater) {
        this.calibrationRecordRepository = calibrationRecordRepository;
        this.deviceCalibrationUpdater = deviceCalibrationUpdater;
    }

    @Override
    public CalibrationRecord handle(RegisterCalibrationRecordCommand command) {
        CalibrationRecord record = CalibrationRecord.register(
                command.deviceId(), command.labConductivityDsM(), command.samplingDate(), command.laboratoryName(),
                command.deviceReadingAtSampling());
        CalibrationRecord saved = calibrationRecordRepository.save(record);
        deviceCalibrationUpdater.applyCalibrationFactor(saved.getDeviceId(), saved.getResultingFactor());
        return saved;
    }
}
