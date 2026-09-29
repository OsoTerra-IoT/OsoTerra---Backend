package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataPlotJpaRepository extends JpaRepository<PlotJpaEntity, Long> {

    List<PlotJpaEntity> findByFarmId(Long farmId);
}
