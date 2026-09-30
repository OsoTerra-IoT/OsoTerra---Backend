package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveActionType;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "corrective_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorrectiveActionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salinity_alert_id", nullable = false, unique = true)
    private Long salinityAlertId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 40)
    private CorrectiveActionType actionType;

    @Column(name = "executed_at", nullable = false)
    private LocalDate executedAt;

    @Column(length = 500)
    private String notes;

    @Column(name = "registered_by", nullable = false)
    private Long registeredBy;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;
}
