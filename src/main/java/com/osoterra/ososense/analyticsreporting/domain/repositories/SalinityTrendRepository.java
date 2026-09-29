package com.osoterra.ososense.analyticsreporting.domain.repositories;

import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;
import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrendId;

import java.util.List;
import java.util.Optional;

public interface SalinityTrendRepository {

    SalinityTrend save(SalinityTrend trend);

    Optional<SalinityTrend> findById(SalinityTrendId id);

    List<SalinityTrend> findByPlotId(Long plotId);
}
