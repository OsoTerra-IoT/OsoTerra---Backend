package com.osoterra.ososense.soilmonitoring.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalibrationRecordTest {

    @Test
    void registerComputesTheResultingFactorAsTheRatioOfLabOverDeviceReading() {
        CalibrationRecord record = CalibrationRecord.register(
                1L, new BigDecimal("4.0"), LocalDate.now(), "Lab Norte", new BigDecimal("5.0"));

        assertThat(record.getResultingFactor()).isEqualByComparingTo("0.8000");
    }

    @Test
    void registerRejectsANonPositiveDeviceReading() {
        assertThatThrownBy(() -> CalibrationRecord.register(
                1L, new BigDecimal("4.0"), LocalDate.now(), "Lab Norte", BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
