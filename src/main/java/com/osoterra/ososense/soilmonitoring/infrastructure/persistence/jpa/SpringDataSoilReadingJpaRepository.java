package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface SpringDataSoilReadingJpaRepository extends JpaRepository<SoilReadingJpaEntity, Long> {

    List<SoilReadingJpaEntity> findByPlotIdOrderByCapturedAtDesc(Long plotId);

    List<SoilReadingJpaEntity> findByPlotIdAndCapturedAtBetween(Long plotId, LocalDateTime start, LocalDateTime end);

    Optional<SoilReadingJpaEntity> findTopByDeviceIdOrderByCapturedAtDesc(Long deviceId);

    @Query("select distinct s.deviceId from SoilReadingJpaEntity s")
    List<Long> findDistinctDeviceIds();
}
