package com.osoterra.ososense.soilmonitoring.interfaces.rest.controllers;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatch;
import com.osoterra.ososense.soilmonitoring.domain.services.ReadingPayload;
import com.osoterra.ososense.soilmonitoring.domain.services.SubmitReadingBatchCommand;
import com.osoterra.ososense.soilmonitoring.domain.services.SubmitReadingBatchCommandService;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.ReadingBatchResource;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.ReadingBatchResourceAssembler;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.SubmitReadingBatchResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Open Host Service the Edge Service pushes telemetry batches to. The device never
 * calls this endpoint directly — only the Edge Service, after buffering and validating
 * locally.
 */
@RestController
@RequestMapping("/api/v1/soil-readings/batches")
class TelemetryIngestionController {

    private final SubmitReadingBatchCommandService submitReadingBatchCommandService;
    private final ReadingBatchResourceAssembler readingBatchResourceAssembler;

    TelemetryIngestionController(
            SubmitReadingBatchCommandService submitReadingBatchCommandService,
            ReadingBatchResourceAssembler readingBatchResourceAssembler) {
        this.submitReadingBatchCommandService = submitReadingBatchCommandService;
        this.readingBatchResourceAssembler = readingBatchResourceAssembler;
    }

    @PostMapping
    ResponseEntity<ReadingBatchResource> submit(@Valid @RequestBody SubmitReadingBatchResource request) {
        var readings = request.readings().stream()
                .map(r -> new ReadingPayload(
                        r.rawConductivityDsM(), r.compensatedConductivityDsM(), r.compensationFactor(),
                        r.moisturePercentage(), r.temperatureCelsius(), r.capturedAt()))
                .toList();
        ReadingBatch batch = submitReadingBatchCommandService.handle(
                new SubmitReadingBatchCommand(request.deviceId(), readings));
        return ResponseEntity.status(HttpStatus.CREATED).body(readingBatchResourceAssembler.toResource(batch));
    }
}
