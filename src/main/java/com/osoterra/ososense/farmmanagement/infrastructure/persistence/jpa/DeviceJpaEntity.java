package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.DeviceStatus;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Persistence model for {@code Device}, kept independent from the domain aggregate.
 */
@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plot_id")
    private Long plotId;

    @Column(name = "activation_code", nullable = false, unique = true, length = 40)
    private String activationCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeviceStatus status;

    @Column(name = "calibration_factor", nullable = false, precision = 6, scale = 4)
    private BigDecimal calibrationFactor;

    @Column(name = "battery_level")
    private Integer batteryLevel;

    @Column(name = "firmware_version", length = 20)
    private String firmwareVersion;

    @Column(name = "reading_interval_minutes", nullable = false)
    private int readingIntervalMinutes;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
