package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionStatus;
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

/**
 * Persistence model for {@code Subscription}, kept independent from the domain aggregate.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_account_id", nullable = false)
    private Long userAccountId;

    @Column(name = "subscription_plan_id", nullable = false)
    private Long subscriptionPlanId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    @Column(name = "quota_total", nullable = false)
    private int quotaTotal;

    @Column(name = "quota_consumed", nullable = false)
    private int quotaConsumed;

    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "period_end_date")
    private LocalDate periodEndDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
