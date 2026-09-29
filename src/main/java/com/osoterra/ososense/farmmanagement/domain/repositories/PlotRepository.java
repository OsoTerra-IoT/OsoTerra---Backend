package com.osoterra.ososense.farmmanagement.domain.repositories;

import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;

import java.util.List;
import java.util.Optional;

public interface PlotRepository {

    Plot save(Plot plot);

    Optional<Plot> findById(PlotId id);

    List<Plot> findByFarmId(FarmId farmId);
}
