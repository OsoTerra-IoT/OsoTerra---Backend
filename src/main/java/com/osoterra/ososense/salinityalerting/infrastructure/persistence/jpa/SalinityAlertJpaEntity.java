package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.AlertSeverity;
import com.osoterra.ososense.salinityalerting.domain.model.AlertStatus;
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

@Entity
@Table(name = "salinity_alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalinityAlertJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plot_id", nullable = false)
    private Long plotId;

    @Column(name = "soil_reading_id", nullable = false)
    private Long soilReadingId;

    @Column(name = "observed_conductivity_ds_m", nullable = false, precision = 6, scale = 3)
    private BigDecimal observedConductivityDsM;

    @Column(name = "applied_threshold_ds_m", nullable = false, precision = 5, scale = 2)
    private BigDecimal appliedThresholdDsM;

    @Column(name = "excess_ratio", nullable = false, precision = 6, scale = 4)
    private BigDecimal excessRatio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertStatus status;

    @Column(name = "acknowledged_by")
    private Long acknowledgedBy;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;
}
