package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataFarmJpaRepository extends JpaRepository<FarmJpaEntity, Long> {

    List<FarmJpaEntity> findByOwnerId(Long ownerId);
}
