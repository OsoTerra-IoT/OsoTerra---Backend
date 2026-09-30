package com.osoterra.ososense.salinityalerting.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationPreferenceTest {

    @Test
    void isSatisfiedByAcceptsASeverityAtOrAboveTheMinimum() {
        NotificationPreference preference = NotificationPreference.register(1L, NotificationChannel.EMAIL, null);
        preference.update(AlertSeverity.WARNING, NotificationChannel.EMAIL, null);

        assertThat(preference.isSatisfiedBy(AlertSeverity.WATCH)).isFalse();
        assertThat(preference.isSatisfiedBy(AlertSeverity.WARNING)).isTrue();
        assertThat(preference.isSatisfiedBy(AlertSeverity.CRITICAL)).isTrue();
    }

    @Test
    void defaultForStartsAtWatchSeverityAndEmailChannel() {
        NotificationPreference preference = NotificationPreference.defaultFor(1L);

        assertThat(preference.getMinimumSeverity()).isEqualTo(AlertSeverity.WATCH);
        assertThat(preference.getChannel()).isEqualTo(NotificationChannel.EMAIL);
    }
}
