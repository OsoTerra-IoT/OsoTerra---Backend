package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import org.springframework.stereotype.Component;

@Component
public class CropResourceAssembler {

    public CropResource toResource(Crop crop) {
        return new CropResource(
                crop.getId().value(),
                crop.getCommonName(),
                crop.getScientificName().orElse(null),
                crop.getSalinityThresholdDsM(),
                crop.getSaltToleranceClass(),
                crop.getSourceReference().orElse(null));
    }
}
