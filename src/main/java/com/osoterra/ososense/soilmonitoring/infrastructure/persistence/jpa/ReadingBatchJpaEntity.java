package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatchStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingBatchJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReadingBatchStatus status;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "accepted_count", nullable = false)
    private int acceptedCount;

    @Column(name = "discarded_count", nullable = false)
    private int discardedCount;
}
