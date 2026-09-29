package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "soil_readings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoilReadingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Column(name = "plot_id", nullable = false)
    private Long plotId;

    @Column(name = "reading_batch_id")
    private Long readingBatchId;

    @Column(name = "raw_conductivity_ds_m", nullable = false, precision = 6, scale = 3)
    private BigDecimal rawConductivityDsM;

    @Column(name = "compensated_conductivity_ds_m", nullable = false, precision = 6, scale = 3)
    private BigDecimal compensatedConductivityDsM;

    @Column(name = "compensation_factor", nullable = false, precision = 6, scale = 4)
    private BigDecimal compensationFactor;

    @Column(name = "moisture_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal moisturePercentage;

    @Column(name = "temperature_celsius", nullable = false, precision = 5, scale = 2)
    private BigDecimal temperatureCelsius;

    @Column(name = "captured_at", nullable = false)
    private LocalDateTime capturedAt;

    @Column(name = "stored_at", nullable = false)
    private LocalDateTime storedAt;
}
