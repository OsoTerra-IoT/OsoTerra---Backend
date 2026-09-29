package com.osoterra.ososense.analyticsreporting.domain.model;

import com.osoterra.ososense.shared.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalinityTrendTest {

    private static final LocalDate START = LocalDate.of(2026, 1, 1);
    private static final LocalDate END = LocalDate.of(2026, 2, 1);

    @Test
    void computeRejectsFewerThanThirtyReadings() {
        assertThatThrownBy(() -> SalinityTrend.compute(1L, START, END, new BigDecimal("0.01"), 29))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void computeRejectsAnEndDateNotAfterTheStartDate() {
        assertThatThrownBy(() -> SalinityTrend.compute(1L, START, START, new BigDecimal("0.01"), 30))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void computeDerivesRisingForAPositiveSlope() {
        SalinityTrend trend = SalinityTrend.compute(1L, START, END, new BigDecimal("0.05"), 30);

        assertThat(trend.getDirection()).isEqualTo(TrendDirection.RISING);
    }

    @Test
    void computeDerivesFallingForANegativeSlope() {
        SalinityTrend trend = SalinityTrend.compute(1L, START, END, new BigDecimal("-0.05"), 30);

        assertThat(trend.getDirection()).isEqualTo(TrendDirection.FALLING);
    }

    @Test
    void computeDerivesStableForASlopeWithinTolerance() {
        SalinityTrend trend = SalinityTrend.compute(1L, START, END, new BigDecimal("0.0001"), 30);

        assertThat(trend.getDirection()).isEqualTo(TrendDirection.STABLE);
    }
}
