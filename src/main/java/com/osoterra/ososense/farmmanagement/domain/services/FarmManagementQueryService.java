package com.osoterra.ososense.farmmanagement.domain.services;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.model.CropId;
import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;

import java.util.List;
import java.util.Optional;

public interface FarmManagementQueryService {

    Optional<Farm> findFarmById(FarmId id);

    List<Farm> findFarmsByOwnerId(Long ownerId);

    Optional<Plot> findPlotById(PlotId id);

    List<Plot> findPlotsByFarmId(FarmId farmId);

    Optional<Device> findDeviceById(DeviceId id);

    List<Crop> findAllCrops();

    Optional<Crop> findCropById(CropId id);
}
