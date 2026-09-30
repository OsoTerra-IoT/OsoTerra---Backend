package com.osoterra.ososense.salinityalerting.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;

import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for one user's alert notification preferences. At most one per user.
 */
public final class NotificationPreference extends AggregateRoot<NotificationPreferenceId> {

    private static final String DEFAULT_LANGUAGE = "en_US";

    private final Long userAccountId;
    private AlertSeverity minimumSeverity;
    private NotificationChannel channel;
    private String pushDeviceToken;
    private String preferredLanguage;

    private NotificationPreference(
            NotificationPreferenceId id, Long userAccountId, AlertSeverity minimumSeverity,
            NotificationChannel channel, String pushDeviceToken, String preferredLanguage) {
        super(id);
        this.userAccountId = userAccountId;
        this.minimumSeverity = minimumSeverity;
        this.channel = channel;
        this.pushDeviceToken = pushDeviceToken;
        this.preferredLanguage = preferredLanguage;
    }

    public static NotificationPreference register(
            Long userAccountId, NotificationChannel channel, String pushDeviceToken) {
        Objects.requireNonNull(userAccountId, "userAccountId");
        Objects.requireNonNull(channel, "channel");
        return new NotificationPreference(
                null, userAccountId, AlertSeverity.WATCH, channel, pushDeviceToken, DEFAULT_LANGUAGE);
    }

    public static NotificationPreference defaultFor(Long userAccountId) {
        return new NotificationPreference(
                null, userAccountId, AlertSeverity.WATCH, NotificationChannel.EMAIL, null, DEFAULT_LANGUAGE);
    }

    public static NotificationPreference reconstruct(
            NotificationPreferenceId id, Long userAccountId, AlertSeverity minimumSeverity,
            NotificationChannel channel, String pushDeviceToken, String preferredLanguage) {
        return new NotificationPreference(id, userAccountId, minimumSeverity, channel, pushDeviceToken, preferredLanguage);
    }

    public void update(AlertSeverity minimumSeverity, NotificationChannel channel, String pushDeviceToken) {
        Objects.requireNonNull(minimumSeverity, "minimumSeverity");
        Objects.requireNonNull(channel, "channel");
        this.minimumSeverity = minimumSeverity;
        this.channel = channel;
        this.pushDeviceToken = pushDeviceToken;
    }

    public boolean isSatisfiedBy(AlertSeverity severity) {
        return severity.ordinal() >= minimumSeverity.ordinal();
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public AlertSeverity getMinimumSeverity() {
        return minimumSeverity;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public Optional<String> getPushDeviceToken() {
        return Optional.ofNullable(pushDeviceToken);
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }
}
