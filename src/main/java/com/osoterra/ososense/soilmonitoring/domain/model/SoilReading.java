package com.osoterra.ososense.soilmonitoring.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;
import com.osoterra.ososense.soilmonitoring.domain.events.SoilReadingStoredEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for one soil sensor reading. The compensation math (temperature and
 * moisture correction to the 25 degC reference) runs in the Edge Service before the
 * reading ever reaches this aggregate — it stores both the raw and already-compensated
 * values so the calculation stays auditable and can be redone if the formula changes.
 */
public final class SoilReading extends AggregateRoot<SoilReadingId> {

    private final Long deviceId;
    private final Long plotId;
    private final ReadingBatchId readingBatchId;
    private final BigDecimal rawConductivityDsM;
    private final BigDecimal compensatedConductivityDsM;
    private final BigDecimal compensationFactor;
    private final BigDecimal moisturePercentage;
    private final BigDecimal temperatureCelsius;
    private final LocalDateTime capturedAt;
    private final LocalDateTime storedAt;

    private SoilReading(
            SoilReadingId id, Long deviceId, Long plotId, ReadingBatchId readingBatchId,
            BigDecimal rawConductivityDsM, BigDecimal compensatedConductivityDsM, BigDecimal compensationFactor,
            BigDecimal moisturePercentage, BigDecimal temperatureCelsius, LocalDateTime capturedAt,
            LocalDateTime storedAt) {
        super(id);
        this.deviceId = deviceId;
        this.plotId = plotId;
        this.readingBatchId = readingBatchId;
        this.rawConductivityDsM = rawConductivityDsM;
        this.compensatedConductivityDsM = compensatedConductivityDsM;
        this.compensationFactor = compensationFactor;
        this.moisturePercentage = moisturePercentage;
        this.temperatureCelsius = temperatureCelsius;
        this.capturedAt = capturedAt;
        this.storedAt = storedAt;
    }

    public static SoilReading capture(
            Long deviceId, Long plotId, ReadingBatchId readingBatchId, BigDecimal rawConductivityDsM,
            BigDecimal compensatedConductivityDsM, BigDecimal compensationFactor, BigDecimal moisturePercentage,
            BigDecimal temperatureCelsius, LocalDateTime capturedAt) {
        Objects.requireNonNull(deviceId, "deviceId");
        Objects.requireNonNull(plotId, "plotId");
        Objects.requireNonNull(rawConductivityDsM, "rawConductivityDsM");
        Objects.requireNonNull(compensatedConductivityDsM, "compensatedConductivityDsM");
        Objects.requireNonNull(moisturePercentage, "moisturePercentage");
        Objects.requireNonNull(capturedAt, "capturedAt");
        if (rawConductivityDsM.signum() < 0 || compensatedConductivityDsM.signum() < 0) {
            throw new IllegalArgumentException("Conductivity values must not be negative");
        }
        if (moisturePercentage.compareTo(BigDecimal.ZERO) < 0 || moisturePercentage.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Moisture percentage must be between 0 and 100");
        }
        if (capturedAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A soil reading cannot be captured in the future");
        }
        SoilReading reading = new SoilReading(
                null, deviceId, plotId, readingBatchId, rawConductivityDsM, compensatedConductivityDsM,
                compensationFactor, moisturePercentage, temperatureCelsius, capturedAt, LocalDateTime.now());
        reading.registerEvent(new SoilReadingStoredEvent(reading.getId(), plotId, compensatedConductivityDsM, Instant.now()));
        return reading;
    }

    public static SoilReading reconstruct(
            SoilReadingId id, Long deviceId, Long plotId, ReadingBatchId readingBatchId,
            BigDecimal rawConductivityDsM, BigDecimal compensatedConductivityDsM, BigDecimal compensationFactor,
            BigDecimal moisturePercentage, BigDecimal temperatureCelsius, LocalDateTime capturedAt,
            LocalDateTime storedAt) {
        return new SoilReading(
                id, deviceId, plotId, readingBatchId, rawConductivityDsM, compensatedConductivityDsM,
                compensationFactor, moisturePercentage, temperatureCelsius, capturedAt, storedAt);
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public Long getPlotId() {
        return plotId;
    }

    public Optional<ReadingBatchId> getReadingBatchId() {
        return Optional.ofNullable(readingBatchId);
    }

    public BigDecimal getRawConductivityDsM() {
        return rawConductivityDsM;
    }

    public BigDecimal getCompensatedConductivityDsM() {
        return compensatedConductivityDsM;
    }

    public BigDecimal getCompensationFactor() {
        return compensationFactor;
    }

    public BigDecimal getMoisturePercentage() {
        return moisturePercentage;
    }

    public BigDecimal getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    public LocalDateTime getStoredAt() {
        return storedAt;
    }
}
