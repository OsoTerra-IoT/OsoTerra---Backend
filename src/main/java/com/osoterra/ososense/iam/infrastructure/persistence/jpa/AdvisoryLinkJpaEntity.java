package com.osoterra.ososense.iam.infrastructure.persistence.jpa;

import com.osoterra.ososense.iam.domain.model.LinkStatus;
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

import java.time.LocalDateTime;

/**
 * Persistence model for {@code AdvisoryLink}, kept independent from the domain aggregate.
 */
@Entity
@Table(name = "advisory_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvisoryLinkJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "advisor_id", nullable = false)
    private Long advisorId;

    @Column(name = "farmer_id", nullable = false)
    private Long farmerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LinkStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
}
