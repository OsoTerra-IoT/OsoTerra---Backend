package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataReadingBatchJpaRepository extends JpaRepository<ReadingBatchJpaEntity, Long> {
}
