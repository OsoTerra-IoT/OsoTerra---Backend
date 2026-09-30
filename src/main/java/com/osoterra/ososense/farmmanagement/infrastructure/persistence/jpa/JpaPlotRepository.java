package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.repositories.PlotRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaPlotRepository implements PlotRepository {

    private final SpringDataPlotJpaRepository springDataRepository;

    JpaPlotRepository(SpringDataPlotJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Plot save(Plot plot) {
        PlotJpaEntity saved = springDataRepository.save(PlotMapper.toEntity(plot));
        return PlotMapper.toDomain(saved);
    }

    @Override
    public Optional<Plot> findById(PlotId id) {
        return springDataRepository.findById(id.value()).map(PlotMapper::toDomain);
    }

    @Override
    public List<Plot> findByFarmId(FarmId farmId) {
        return springDataRepository.findByFarmId(farmId.value()).stream().map(PlotMapper::toDomain).toList();
    }
}
