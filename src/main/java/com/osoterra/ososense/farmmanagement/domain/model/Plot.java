package com.osoterra.ososense.farmmanagement.domain.model;

import com.osoterra.ososense.farmmanagement.domain.events.CropAssignedToPlotEvent;
import com.osoterra.ososense.shared.AggregateRoot;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for a plot conducted within a farm. Owns its crop assignment and
 * active/inactive lifecycle; the salinity threshold that applies to it is looked up
 * from the assigned {@link Crop}, never duplicated here.
 */
public final class Plot extends AggregateRoot<PlotId> {

    private final FarmId farmId;
    private CropId cropId;
    private final String name;
    private final BigDecimal areaHectares;
    private final GeoCoordinates coordinates;
    private boolean isActive;
    private final LocalDateTime createdAt;

    private Plot(
            PlotId id, FarmId farmId, CropId cropId, String name, BigDecimal areaHectares,
            GeoCoordinates coordinates, boolean isActive, LocalDateTime createdAt) {
        super(id);
        this.farmId = farmId;
        this.cropId = cropId;
        this.name = name;
        this.areaHectares = areaHectares;
        this.coordinates = coordinates;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public static Plot register(FarmId farmId, String name, BigDecimal areaHectares, GeoCoordinates coordinates) {
        Objects.requireNonNull(farmId, "farmId");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(areaHectares, "areaHectares");
        Objects.requireNonNull(coordinates, "coordinates");
        if (name.isBlank()) {
            throw new IllegalArgumentException("A plot's name must not be blank");
        }
        if (areaHectares.signum() <= 0) {
            throw new IllegalArgumentException("A plot's area must be greater than zero");
        }
        return new Plot(null, farmId, null, name, areaHectares, coordinates, true, LocalDateTime.now());
    }

    public static Plot reconstruct(
            PlotId id, FarmId farmId, CropId cropId, String name, BigDecimal areaHectares,
            GeoCoordinates coordinates, boolean isActive, LocalDateTime createdAt) {
        return new Plot(id, farmId, cropId, name, areaHectares, coordinates, isActive, createdAt);
    }

    public void assignCrop(CropId cropId) {
        Objects.requireNonNull(cropId, "cropId");
        this.cropId = cropId;
        registerEvent(new CropAssignedToPlotEvent(getId(), cropId, Instant.now()));
    }

    public void deactivate() {
        this.isActive = false;
    }

    public FarmId getFarmId() {
        return farmId;
    }

    public Optional<CropId> getCropId() {
        return Optional.ofNullable(cropId);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAreaHectares() {
        return areaHectares;
    }

    public GeoCoordinates getCoordinates() {
        return coordinates;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
