package com.osoterra.ososense.soilmonitoring.domain.repositories;

import com.osoterra.ososense.soilmonitoring.domain.model.SoilReading;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReadingId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SoilReadingRepository {

    SoilReading save(SoilReading reading);

    Optional<SoilReading> findById(SoilReadingId id);

    List<SoilReading> findByPlotIdOrderByCapturedAtDesc(Long plotId);

    List<SoilReading> findByPlotIdAndCapturedAtBetween(Long plotId, LocalDateTime start, LocalDateTime end);

    Optional<SoilReading> findTopByDeviceIdOrderByCapturedAtDesc(Long deviceId);

    List<Long> findDistinctDeviceIds();
}
