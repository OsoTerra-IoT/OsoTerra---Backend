package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataReportSectionJpaRepository extends JpaRepository<ReportSectionJpaEntity, Long> {

    List<ReportSectionJpaEntity> findByPlotReportIdOrderByDisplayOrderAsc(Long plotReportId);
}
