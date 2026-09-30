package com.osoterra.ososense.farmmanagement.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for a crop catalog entry. Reference data preloaded by
 * {@code CropCatalogSeeder} before the rest of the platform can operate — a plot cannot
 * generate salinity alerts without a crop's tolerance threshold.
 */
public final class Crop extends AggregateRoot<CropId> {

    private final String commonName;
    private final String scientificName;
    private final BigDecimal salinityThresholdDsM;
    private final String saltToleranceClass;
    private final String sourceReference;

    private Crop(
            CropId id,
            String commonName,
            String scientificName,
            BigDecimal salinityThresholdDsM,
            String saltToleranceClass,
            String sourceReference) {
        super(id);
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.salinityThresholdDsM = salinityThresholdDsM;
        this.saltToleranceClass = saltToleranceClass;
        this.sourceReference = sourceReference;
    }

    public static Crop register(
            String commonName,
            String scientificName,
            BigDecimal salinityThresholdDsM,
            String saltToleranceClass,
            String sourceReference) {
        Objects.requireNonNull(commonName, "commonName");
        Objects.requireNonNull(salinityThresholdDsM, "salinityThresholdDsM");
        Objects.requireNonNull(saltToleranceClass, "saltToleranceClass");
        if (commonName.isBlank()) {
            throw new IllegalArgumentException("A crop's common name must not be blank");
        }
        if (salinityThresholdDsM.signum() <= 0) {
            throw new IllegalArgumentException("A crop's salinity threshold must be greater than zero");
        }
        return new Crop(null, commonName, scientificName, salinityThresholdDsM, saltToleranceClass, sourceReference);
    }

    public static Crop reconstruct(
            CropId id,
            String commonName,
            String scientificName,
            BigDecimal salinityThresholdDsM,
            String saltToleranceClass,
            String sourceReference) {
        return new Crop(id, commonName, scientificName, salinityThresholdDsM, saltToleranceClass, sourceReference);
    }

    public String getCommonName() {
        return commonName;
    }

    public Optional<String> getScientificName() {
        return Optional.ofNullable(scientificName);
    }

    public BigDecimal getSalinityThresholdDsM() {
        return salinityThresholdDsM;
    }

    public String getSaltToleranceClass() {
        return saltToleranceClass;
    }

    public Optional<String> getSourceReference() {
        return Optional.ofNullable(sourceReference);
    }
}
