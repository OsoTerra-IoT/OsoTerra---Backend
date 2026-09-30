package com.osoterra.ososense.soilmonitoring.application.internal.commandservices;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import com.osoterra.ososense.soilmonitoring.domain.repositories.CalibrationRecordRepository;
import com.osoterra.ososense.soilmonitoring.domain.services.RegisterCalibrationRecordCommand;
import com.osoterra.ososense.soilmonitoring.domain.services.RegisterCalibrationRecordCommandService;
import org.springframework.stereotype.Service;

@Service
class RegisterCalibrationRecordCommandServiceImpl implements RegisterCalibrationRecordCommandService {

    private final CalibrationRecordRepository calibrationRecordRepository;

    RegisterCalibrationRecordCommandServiceImpl(CalibrationRecordRepository calibrationRecordRepository) {
        this.calibrationRecordRepository = calibrationRecordRepository;
    }

    @Override
    public CalibrationRecord handle(RegisterCalibrationRecordCommand command) {
        CalibrationRecord record = CalibrationRecord.register(
                command.deviceId(), command.labConductivityDsM(), command.samplingDate(), command.laboratoryName(),
                command.deviceReadingAtSampling());
        return calibrationRecordRepository.save(record);
    }
}
