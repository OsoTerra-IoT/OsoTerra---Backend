package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataDeviceJpaRepository extends JpaRepository<DeviceJpaEntity, Long> {

    Optional<DeviceJpaEntity> findByActivationCode(String activationCode);

    boolean existsByActivationCode(String activationCode);
}
