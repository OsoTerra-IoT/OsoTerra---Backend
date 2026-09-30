package com.osoterra.ososense.salinityalerting.domain.model;

import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalinityAlertTest {

    @Test
    void generateRejectsAnObservedValueThatDoesNotExceedTheThreshold() {
        assertThatThrownBy(() -> SalinityAlert.generate(1L, 2L, new BigDecimal("3.0"), new BigDecimal("3.0")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void generateDerivesWatchSeverityForASmallExcess() {
        SalinityAlert alert = SalinityAlert.generate(1L, 2L, new BigDecimal("3.1"), new BigDecimal("3.0"));

        assertThat(alert.getSeverity()).isEqualTo(AlertSeverity.WATCH);
        assertThat(alert.getStatus()).isEqualTo(AlertStatus.OPEN);
    }

    @Test
    void generateDerivesWarningSeverityForAModerateExcess() {
        SalinityAlert alert = SalinityAlert.generate(1L, 2L, new BigDecimal("3.9"), new BigDecimal("3.0"));

        assertThat(alert.getSeverity()).isEqualTo(AlertSeverity.WARNING);
    }

    @Test
    void generateDerivesCriticalSeverityForALargeExcess() {
        SalinityAlert alert = SalinityAlert.generate(1L, 2L, new BigDecimal("6.0"), new BigDecimal("3.0"));

        assertThat(alert.getSeverity()).isEqualTo(AlertSeverity.CRITICAL);
    }

    @Test
    void acknowledgeRejectsAnAlertThatIsNotOpen() {
        SalinityAlert alert = SalinityAlert.generate(1L, 2L, new BigDecimal("6.0"), new BigDecimal("3.0"));
        alert.acknowledge(9L);

        assertThatThrownBy(() -> alert.acknowledge(9L)).isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void resolveRejectsAnAlreadyResolvedAlert() {
        SalinityAlert alert = SalinityAlert.generate(1L, 2L, new BigDecimal("6.0"), new BigDecimal("3.0"));
        alert.resolve();

        assertThatThrownBy(alert::resolve).isInstanceOf(BusinessRuleViolationException.class);
    }
}
