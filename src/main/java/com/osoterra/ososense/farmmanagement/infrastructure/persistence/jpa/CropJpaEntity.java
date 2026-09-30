package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

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

/**
 * Persistence model for {@code Crop}, kept independent from the domain aggregate.
 */
@Entity
@Table(name = "crops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CropJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "common_name", nullable = false, unique = true, length = 80)
    private String commonName;

    @Column(name = "scientific_name", length = 120)
    private String scientificName;

    @Column(name = "salinity_threshold_ds_m", nullable = false, precision = 5, scale = 2)
    private BigDecimal salinityThresholdDsM;

    @Column(name = "salt_tolerance_class", nullable = false, length = 30)
    private String saltToleranceClass;

    @Column(name = "source_reference", length = 200)
    private String sourceReference;
}
