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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "calibration_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalibrationRecordJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Column(name = "lab_conductivity_ds_m", nullable = false, precision = 6, scale = 3)
    private BigDecimal labConductivityDsM;

    @Column(name = "sampling_date", nullable = false)
    private LocalDate samplingDate;

    @Column(name = "laboratory_name", nullable = false, length = 120)
    private String laboratoryName;

    @Column(name = "device_reading_at_sampling", nullable = false, precision = 6, scale = 3)
    private BigDecimal deviceReadingAtSampling;

    @Column(name = "resulting_factor", nullable = false, precision = 6, scale = 4)
    private BigDecimal resultingFactor;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;
}
