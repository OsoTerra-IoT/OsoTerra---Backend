package com.osoterra.ososense.soilmonitoring.domain.model;

import com.osoterra.ososense.soilmonitoring.domain.events.SoilReadingStoredEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoilReadingTest {

    @Test
    void captureStoresTheReadingAndPublishesSoilReadingStoredEvent() {
        SoilReading reading = SoilReading.capture(
                1L, 2L, null, new BigDecimal("3.5"), new BigDecimal("3.2"), new BigDecimal("1.05"),
                new BigDecimal("40.0"), new BigDecimal("22.0"), LocalDateTime.now().minusMinutes(1));

        assertThat(reading.getDeviceId()).isEqualTo(1L);
        assertThat(reading.getPlotId()).isEqualTo(2L);
        var events = reading.pullDomainEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOfSatisfying(
                SoilReadingStoredEvent.class, event -> assertThat(event.plotId()).isEqualTo(2L));
    }

    @Test
    void captureRejectsANegativeConductivity() {
        assertThatThrownBy(() -> SoilReading.capture(
                1L, 2L, null, new BigDecimal("-1"), new BigDecimal("3.2"), new BigDecimal("1.05"),
                new BigDecimal("40.0"), new BigDecimal("22.0"), LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void captureRejectsAMoistureOutsideZeroToOneHundred() {
        assertThatThrownBy(() -> SoilReading.capture(
                1L, 2L, null, new BigDecimal("3.5"), new BigDecimal("3.2"), new BigDecimal("1.05"),
                new BigDecimal("101"), new BigDecimal("22.0"), LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void captureRejectsAFutureCapturedAt() {
        assertThatThrownBy(() -> SoilReading.capture(
                1L, 2L, null, new BigDecimal("3.5"), new BigDecimal("3.2"), new BigDecimal("1.05"),
                new BigDecimal("40.0"), new BigDecimal("22.0"), LocalDateTime.now().plusDays(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
