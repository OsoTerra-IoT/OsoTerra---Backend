package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.CropId;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.GeoCoordinates;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;

final class PlotMapper {

    private PlotMapper() {
    }

    static Plot toDomain(PlotJpaEntity entity) {
        CropId cropId = entity.getCropId() == null ? null : new CropId(entity.getCropId());
        return Plot.reconstruct(
                new PlotId(entity.getId()),
                new FarmId(entity.getFarmId()),
                cropId,
                entity.getName(),
                entity.getAreaHectares(),
                new GeoCoordinates(entity.getLatitude(), entity.getLongitude()),
                entity.isActive(),
                entity.getCreatedAt());
    }

    static PlotJpaEntity toEntity(Plot plot) {
        Long id = plot.getId() == null ? null : plot.getId().value();
        Long cropId = plot.getCropId().map(CropId::value).orElse(null);
        return new PlotJpaEntity(
                id,
                plot.getFarmId().value(),
                cropId,
                plot.getName(),
                plot.getAreaHectares(),
                plot.getCoordinates().latitude(),
                plot.getCoordinates().longitude(),
                plot.isActive(),
                plot.getCreatedAt());
    }
}
