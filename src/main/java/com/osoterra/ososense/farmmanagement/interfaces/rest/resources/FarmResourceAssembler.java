package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import org.springframework.stereotype.Component;

@Component
public class FarmResourceAssembler {

    public FarmResource toResource(Farm farm) {
        return new FarmResource(
                farm.getId().value(),
                farm.getOwnerId(),
                farm.getName(),
                farm.getDepartment(),
                farm.getProvince(),
                farm.getDistrict(),
                farm.getCreatedAt());
    }
}
