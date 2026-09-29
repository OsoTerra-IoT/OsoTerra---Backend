package com.osoterra.ososense.farmmanagement.infrastructure;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.repositories.CropRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Preloads the crop salinity tolerance catalog (Maas-Hoffman thresholds) the platform
 * needs before it can operate — a plot without an assigned crop cannot generate salinity
 * alerts. Idempotent: skips any crop that already exists.
 */
@Component
public class CropCatalogSeeder implements ApplicationRunner {

    private record CatalogEntry(String commonName, BigDecimal salinityThresholdDsM, String saltToleranceClass) {
    }

    private static final List<CatalogEntry> CATALOG = List.of(
            new CatalogEntry("Cebada", new BigDecimal("8.0"), "Tolerante"),
            new CatalogEntry("Algodón", new BigDecimal("7.7"), "Tolerante"),
            new CatalogEntry("Remolacha azucarera", new BigDecimal("7.0"), "Tolerante"),
            new CatalogEntry("Trigo", new BigDecimal("6.0"), "Moderadamente tolerante"),
            new CatalogEntry("Espárrago", new BigDecimal("5.0"), "Tolerante"),
            new CatalogEntry("Arroz", new BigDecimal("3.0"), "Moderadamente sensible"),
            new CatalogEntry("Tomate", new BigDecimal("2.5"), "Moderadamente sensible"),
            new CatalogEntry("Maíz", new BigDecimal("1.7"), "Sensible"),
            new CatalogEntry("Papa", new BigDecimal("1.7"), "Sensible"),
            new CatalogEntry("Uva", new BigDecimal("1.5"), "Sensible"),
            new CatalogEntry("Cebolla", new BigDecimal("1.2"), "Sensible"),
            new CatalogEntry("Fresa", new BigDecimal("1.0"), "Sensible"));

    private final CropRepository cropRepository;

    public CropCatalogSeeder(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (CatalogEntry entry : CATALOG) {
            if (!cropRepository.existsByCommonName(entry.commonName())) {
                cropRepository.save(Crop.register(
                        entry.commonName(), null, entry.salinityThresholdDsM(), entry.saltToleranceClass(), null));
            }
        }
    }
}
