package com.osoterra.ososense.salinityalerting.domain.repositories;

import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;

import java.util.List;
import java.util.Optional;

public interface SalinityAlertRepository {

    SalinityAlert save(SalinityAlert alert);

    Optional<SalinityAlert> findById(SalinityAlertId id);

    List<SalinityAlert> findByPlotId(Long plotId);

    Optional<SalinityAlert> findLatestByPlotId(Long plotId);
}
