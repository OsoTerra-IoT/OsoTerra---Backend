package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataCropJpaRepository extends JpaRepository<CropJpaEntity, Long> {

    Optional<CropJpaEntity> findByCommonName(String commonName);

    boolean existsByCommonName(String commonName);
}
