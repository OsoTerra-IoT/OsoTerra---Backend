package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataPlotReportJpaRepository extends JpaRepository<PlotReportJpaEntity, Long> {

    List<PlotReportJpaEntity> findByPlotId(Long plotId);
}
