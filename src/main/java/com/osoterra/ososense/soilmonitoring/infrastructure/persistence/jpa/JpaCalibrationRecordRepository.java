package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecordId;
import com.osoterra.ososense.soilmonitoring.domain.repositories.CalibrationRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaCalibrationRecordRepository implements CalibrationRecordRepository {

    private final SpringDataCalibrationRecordJpaRepository springDataRepository;

    JpaCalibrationRecordRepository(SpringDataCalibrationRecordJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public CalibrationRecord save(CalibrationRecord record) {
        CalibrationRecordJpaEntity saved = springDataRepository.save(CalibrationRecordMapper.toEntity(record));
        return CalibrationRecordMapper.toDomain(saved);
    }

    @Override
    public Optional<CalibrationRecord> findById(CalibrationRecordId id) {
        return springDataRepository.findById(id.value()).map(CalibrationRecordMapper::toDomain);
    }

    @Override
    public List<CalibrationRecord> findByDeviceId(Long deviceId) {
        return springDataRepository.findByDeviceId(deviceId).stream().map(CalibrationRecordMapper::toDomain).toList();
    }
}
