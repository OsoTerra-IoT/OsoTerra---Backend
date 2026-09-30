package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import com.osoterra.ososense.analyticsreporting.domain.model.TrendDirection;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "salinity_trends")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalinityTrendJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plot_id", nullable = false)
    private Long plotId;

    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEndDate;

    @Column(name = "slope_ds_m_per_day", nullable = false, precision = 8, scale = 5)
    private BigDecimal slopeDsMPerDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrendDirection direction;

    @Column(name = "reading_count", nullable = false)
    private int readingCount;

    @Column(name = "computed_at", nullable = false)
    private LocalDateTime computedAt;
}
