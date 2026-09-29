package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataCalibrationRecordJpaRepository extends JpaRepository<CalibrationRecordJpaEntity, Long> {

    List<CalibrationRecordJpaEntity> findByDeviceId(Long deviceId);
}
