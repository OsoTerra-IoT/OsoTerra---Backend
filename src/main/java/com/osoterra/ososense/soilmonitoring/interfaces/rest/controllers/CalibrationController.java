package com.osoterra.ososense.soilmonitoring.interfaces.rest.controllers;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import com.osoterra.ososense.soilmonitoring.domain.services.RegisterCalibrationRecordCommand;
import com.osoterra.ososense.soilmonitoring.domain.services.RegisterCalibrationRecordCommandService;
import com.osoterra.ososense.soilmonitoring.domain.services.SoilMonitoringQueryService;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.CalibrationRecordResource;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.CalibrationRecordResourceAssembler;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.RegisterCalibrationRecordResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Registers and lists laboratory calibration samples for a device. Also covers the
 * report's "lab result" use case — the same underlying data, one controller.
 */
@RestController
@RequestMapping("/api/v1/calibration-records")
class CalibrationController {

    private final RegisterCalibrationRecordCommandService registerCalibrationRecordCommandService;
    private final SoilMonitoringQueryService soilMonitoringQueryService;
    private final CalibrationRecordResourceAssembler calibrationRecordResourceAssembler;

    CalibrationController(
            RegisterCalibrationRecordCommandService registerCalibrationRecordCommandService,
            SoilMonitoringQueryService soilMonitoringQueryService,
            CalibrationRecordResourceAssembler calibrationRecordResourceAssembler) {
        this.registerCalibrationRecordCommandService = registerCalibrationRecordCommandService;
        this.soilMonitoringQueryService = soilMonitoringQueryService;
        this.calibrationRecordResourceAssembler = calibrationRecordResourceAssembler;
    }

    @PostMapping
    ResponseEntity<CalibrationRecordResource> register(@Valid @RequestBody RegisterCalibrationRecordResource request) {
        CalibrationRecord record = registerCalibrationRecordCommandService.handle(new RegisterCalibrationRecordCommand(
                request.deviceId(), request.labConductivityDsM(), request.samplingDate(), request.laboratoryName(),
                request.deviceReadingAtSampling()));
        return ResponseEntity.status(HttpStatus.CREATED).body(calibrationRecordResourceAssembler.toResource(record));
    }

    @GetMapping
    List<CalibrationRecordResource> byDevice(@RequestParam Long deviceId) {
        return soilMonitoringQueryService.findCalibrationRecordsByDeviceId(deviceId).stream()
                .map(calibrationRecordResourceAssembler::toResource)
                .toList();
    }
}
