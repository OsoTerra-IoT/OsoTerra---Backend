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
import java.time.LocalDateTime;

/**
 * Persistence model for {@code Plot}, kept independent from the domain aggregate.
 */
@Entity
@Table(name = "plots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlotJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "crop_id")
    private Long cropId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "area_hectares", nullable = false, precision = 8, scale = 2)
    private BigDecimal areaHectares;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
