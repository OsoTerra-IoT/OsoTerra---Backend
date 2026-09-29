package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import com.osoterra.ososense.soilmonitoring.domain.model.SoilReading;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReadingId;
import com.osoterra.ososense.soilmonitoring.domain.repositories.SoilReadingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
class JpaSoilReadingRepository implements SoilReadingRepository {

    private final SpringDataSoilReadingJpaRepository springDataRepository;

    JpaSoilReadingRepository(SpringDataSoilReadingJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public SoilReading save(SoilReading reading) {
        SoilReadingJpaEntity saved = springDataRepository.save(SoilReadingMapper.toEntity(reading));
        return SoilReadingMapper.toDomain(saved);
    }

    @Override
    public Optional<SoilReading> findById(SoilReadingId id) {
        return springDataRepository.findById(id.value()).map(SoilReadingMapper::toDomain);
    }

    @Override
    public List<SoilReading> findByPlotIdOrderByCapturedAtDesc(Long plotId) {
        return springDataRepository.findByPlotIdOrderByCapturedAtDesc(plotId).stream()
                .map(SoilReadingMapper::toDomain)
                .toList();
    }

    @Override
    public List<SoilReading> findByPlotIdAndCapturedAtBetween(Long plotId, LocalDateTime start, LocalDateTime end) {
        return springDataRepository.findByPlotIdAndCapturedAtBetween(plotId, start, end).stream()
                .map(SoilReadingMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<SoilReading> findTopByDeviceIdOrderByCapturedAtDesc(Long deviceId) {
        return springDataRepository.findTopByDeviceIdOrderByCapturedAtDesc(deviceId).map(SoilReadingMapper::toDomain);
    }

    @Override
    public List<Long> findDistinctDeviceIds() {
        return springDataRepository.findDistinctDeviceIds();
    }
}
