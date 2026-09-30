package com.osoterra.ososense.soilmonitoring.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregate root tracking one ingestion batch submitted by the Edge Service, so a
 * reprocessed batch after a connectivity drop can be recognized and not double-counted.
 */
public final class ReadingBatch extends AggregateRoot<ReadingBatchId> {

    private final Long deviceId;
    private ReadingBatchStatus status;
    private final LocalDateTime submittedAt;
    private int acceptedCount;
    private int discardedCount;

    private ReadingBatch(
            ReadingBatchId id, Long deviceId, ReadingBatchStatus status, LocalDateTime submittedAt,
            int acceptedCount, int discardedCount) {
        super(id);
        this.deviceId = deviceId;
        this.status = status;
        this.submittedAt = submittedAt;
        this.acceptedCount = acceptedCount;
        this.discardedCount = discardedCount;
    }

    public static ReadingBatch submit(Long deviceId) {
        Objects.requireNonNull(deviceId, "deviceId");
        return new ReadingBatch(null, deviceId, ReadingBatchStatus.PENDING, LocalDateTime.now(), 0, 0);
    }

    public static ReadingBatch reconstruct(
            ReadingBatchId id, Long deviceId, ReadingBatchStatus status, LocalDateTime submittedAt,
            int acceptedCount, int discardedCount) {
        return new ReadingBatch(id, deviceId, status, submittedAt, acceptedCount, discardedCount);
    }

    public void markSynchronized(int accepted, int discarded) {
        if (status != ReadingBatchStatus.PENDING) {
            throw new BusinessRuleViolationException("Only a pending batch can be marked synchronized");
        }
        this.status = ReadingBatchStatus.SYNCHRONIZED;
        this.acceptedCount = accepted;
        this.discardedCount = discarded;
    }

    public void discard() {
        this.status = ReadingBatchStatus.DISCARDED;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public ReadingBatchStatus getStatus() {
        return status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public int getAcceptedCount() {
        return acceptedCount;
    }

    public int getDiscardedCount() {
        return discardedCount;
    }
}
