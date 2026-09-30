package com.osoterra.ososense.soilmonitoring.interfaces.rest.controllers;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatch;
import com.osoterra.ososense.soilmonitoring.domain.services.ReadingPayload;
import com.osoterra.ososense.soilmonitoring.domain.services.SubmitReadingBatchCommand;
import com.osoterra.ososense.soilmonitoring.domain.services.SubmitReadingBatchCommandService;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.ReadingBatchResource;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.ReadingBatchResourceAssembler;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.SubmitReadingBatchResource;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Open Host Service the Edge Service pushes telemetry batches to. The device never
 * calls this endpoint directly — only the Edge Service, after buffering and validating
 * locally, and it authenticates with the shared key sent in {@code X-Edge-Api-Key}
 * instead of a user token.
 */
@RestController
@RequestMapping("/api/v1/soil-readings/batches")
class TelemetryIngestionController {

    private final SubmitReadingBatchCommandService submitReadingBatchCommandService;
    private final ReadingBatchResourceAssembler readingBatchResourceAssembler;
    private final byte[] edgeApiKey;

    TelemetryIngestionController(
            SubmitReadingBatchCommandService submitReadingBatchCommandService,
            ReadingBatchResourceAssembler readingBatchResourceAssembler,
            @Value("${app.edge.api-key}") String edgeApiKey) {
        this.submitReadingBatchCommandService = submitReadingBatchCommandService;
        this.readingBatchResourceAssembler = readingBatchResourceAssembler;
        this.edgeApiKey = edgeApiKey.getBytes(StandardCharsets.UTF_8);
    }

    @PostMapping
    ResponseEntity<ReadingBatchResource> submit(
            @RequestHeader(name = "X-Edge-Api-Key", required = false) String apiKey,
            @Valid @RequestBody SubmitReadingBatchResource request) {
        if (apiKey == null || !MessageDigest.isEqual(edgeApiKey, apiKey.getBytes(StandardCharsets.UTF_8))) {
            throw new BadCredentialsException("A valid X-Edge-Api-Key header is required");
        }
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
