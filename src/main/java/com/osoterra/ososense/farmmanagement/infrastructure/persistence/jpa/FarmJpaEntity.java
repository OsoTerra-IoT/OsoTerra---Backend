package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Persistence model for {@code Farm}, kept independent from the domain aggregate.
 * {@code ownerId} references {@code user_accounts} in the IAM bounded context by raw id
 * only, never through a JPA association.
 */
@Entity
@Table(name = "farms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 60)
    private String department;

    @Column(nullable = false, length = 60)
    private String province;

    @Column(nullable = false, length = 60)
    private String district;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
