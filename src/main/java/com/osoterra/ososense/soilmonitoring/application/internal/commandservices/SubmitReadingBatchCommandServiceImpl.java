package com.osoterra.ososense.soilmonitoring.application.internal.commandservices;

import com.osoterra.ososense.salinityalerting.domain.services.EvaluateReadingCommand;
import com.osoterra.ososense.salinityalerting.domain.services.EvaluateReadingCommandService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import com.osoterra.ososense.soilmonitoring.domain.gateways.DeviceActivityRecorder;
import com.osoterra.ososense.soilmonitoring.domain.gateways.DeviceLocationLookup;
import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatch;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReading;
import com.osoterra.ososense.soilmonitoring.domain.repositories.ReadingBatchRepository;
import com.osoterra.ososense.soilmonitoring.domain.repositories.SoilReadingRepository;
import com.osoterra.ososense.soilmonitoring.domain.services.ReadingPayload;
import com.osoterra.ososense.soilmonitoring.domain.services.SubmitReadingBatchCommand;
import com.osoterra.ososense.soilmonitoring.domain.services.SubmitReadingBatchCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Ingests one telemetry batch from the Edge Service. Physically impossible readings are
 * discarded individually rather than failing the whole batch. Each accepted reading is
 * evaluated against its plot's salinity threshold right away, synchronously, in the same
 * transaction-adjacent call — see {@code EvaluateReadingCommandService} for why this is a
 * direct call rather than a published event. The newest accepted reading is reported to
 * Farm Management as the device's last-seen time.
 */
@Service
@Transactional
class SubmitReadingBatchCommandServiceImpl implements SubmitReadingBatchCommandService {

    private final ReadingBatchRepository readingBatchRepository;
    private final SoilReadingRepository soilReadingRepository;
    private final DeviceLocationLookup deviceLocationLookup;
    private final DeviceActivityRecorder deviceActivityRecorder;
    private final EvaluateReadingCommandService evaluateReadingCommandService;

    SubmitReadingBatchCommandServiceImpl(
            ReadingBatchRepository readingBatchRepository,
            SoilReadingRepository soilReadingRepository,
            DeviceLocationLookup deviceLocationLookup,
            DeviceActivityRecorder deviceActivityRecorder,
            EvaluateReadingCommandService evaluateReadingCommandService) {
        this.readingBatchRepository = readingBatchRepository;
        this.soilReadingRepository = soilReadingRepository;
        this.deviceLocationLookup = deviceLocationLookup;
        this.deviceActivityRecorder = deviceActivityRecorder;
        this.evaluateReadingCommandService = evaluateReadingCommandService;
    }

    @Override
    public ReadingBatch handle(SubmitReadingBatchCommand command) {
        Long plotId = deviceLocationLookup
                .findPlotIdForDevice(command.deviceId())
                .orElseThrow(() -> new EntityNotFoundException("No plot found for device id " + command.deviceId()));

        ReadingBatch batch = readingBatchRepository.save(ReadingBatch.submit(command.deviceId()));

        int accepted = 0;
        int discarded = 0;
        LocalDateTime newestCapturedAt = null;
        for (ReadingPayload payload : command.readings()) {
            try {
                SoilReading reading = SoilReading.capture(
                        command.deviceId(), plotId, batch.getId(), payload.rawConductivityDsM(),
                        payload.compensatedConductivityDsM(), payload.compensationFactor(),
                        payload.moisturePercentage(), payload.temperatureCelsius(), payload.capturedAt());
                reading = soilReadingRepository.save(reading);
                accepted++;
                if (newestCapturedAt == null || reading.getCapturedAt().isAfter(newestCapturedAt)) {
                    newestCapturedAt = reading.getCapturedAt();
                }
                evaluateReadingCommandService.handle(new EvaluateReadingCommand(
                        plotId, reading.getId().value(), reading.getCompensatedConductivityDsM()));
            } catch (IllegalArgumentException invalidReading) {
                discarded++;
            }
        }

        if (newestCapturedAt != null) {
            deviceActivityRecorder.recordReading(command.deviceId(), newestCapturedAt);
        }
        batch.markSynchronized(accepted, discarded);
        return readingBatchRepository.save(batch);
    }
}
