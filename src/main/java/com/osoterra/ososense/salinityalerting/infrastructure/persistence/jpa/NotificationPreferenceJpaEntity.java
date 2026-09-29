package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.AlertSeverity;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationChannel;
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

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_account_id", nullable = false, unique = true)
    private Long userAccountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "minimum_severity", nullable = false, length = 20)
    private AlertSeverity minimumSeverity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationChannel channel;

    @Column(name = "push_device_token", length = 255)
    private String pushDeviceToken;

    @Column(name = "preferred_language", nullable = false, length = 10)
    private String preferredLanguage;
}
