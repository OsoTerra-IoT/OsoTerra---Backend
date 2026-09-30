package com.osoterra.ososense.soilmonitoring.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregate root for a laboratory calibration sample, used to derive the correction
 * factor a device's future readings should apply.
 */
public final class CalibrationRecord extends AggregateRoot<CalibrationRecordId> {

    private final Long deviceId;
    private final BigDecimal labConductivityDsM;
    private final LocalDate samplingDate;
    private final String laboratoryName;
    private final BigDecimal deviceReadingAtSampling;
    private final BigDecimal resultingFactor;
    private final LocalDateTime registeredAt;

    private CalibrationRecord(
            CalibrationRecordId id, Long deviceId, BigDecimal labConductivityDsM, LocalDate samplingDate,
            String laboratoryName, BigDecimal deviceReadingAtSampling, BigDecimal resultingFactor,
            LocalDateTime registeredAt) {
        super(id);
        this.deviceId = deviceId;
        this.labConductivityDsM = labConductivityDsM;
        this.samplingDate = samplingDate;
        this.laboratoryName = laboratoryName;
        this.deviceReadingAtSampling = deviceReadingAtSampling;
        this.resultingFactor = resultingFactor;
        this.registeredAt = registeredAt;
    }

    /**
     * Registers a calibration sample and derives the resulting correction factor as the
     * ratio between the certified laboratory value and what the device read at the same
     * moment.
     */
    public static CalibrationRecord register(
            Long deviceId, BigDecimal labConductivityDsM, LocalDate samplingDate, String laboratoryName,
            BigDecimal deviceReadingAtSampling) {
        Objects.requireNonNull(deviceId, "deviceId");
        Objects.requireNonNull(labConductivityDsM, "labConductivityDsM");
        Objects.requireNonNull(deviceReadingAtSampling, "deviceReadingAtSampling");
        if (deviceReadingAtSampling.signum() <= 0) {
            throw new IllegalArgumentException("The device reading at sampling must be greater than zero");
        }
        BigDecimal resultingFactor = labConductivityDsM.divide(deviceReadingAtSampling, 4, RoundingMode.HALF_UP);
        return new CalibrationRecord(
                null, deviceId, labConductivityDsM, samplingDate, laboratoryName, deviceReadingAtSampling,
                resultingFactor, LocalDateTime.now());
    }

    public static CalibrationRecord reconstruct(
            CalibrationRecordId id, Long deviceId, BigDecimal labConductivityDsM, LocalDate samplingDate,
            String laboratoryName, BigDecimal deviceReadingAtSampling, BigDecimal resultingFactor,
            LocalDateTime registeredAt) {
        return new CalibrationRecord(
                id, deviceId, labConductivityDsM, samplingDate, laboratoryName, deviceReadingAtSampling,
                resultingFactor, registeredAt);
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public BigDecimal getLabConductivityDsM() {
        return labConductivityDsM;
    }

    public LocalDate getSamplingDate() {
        return samplingDate;
    }

    public String getLaboratoryName() {
        return laboratoryName;
    }

    public BigDecimal getDeviceReadingAtSampling() {
        return deviceReadingAtSampling;
    }

    public BigDecimal getResultingFactor() {
        return resultingFactor;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
}
