package com.osoterra.ososense.farmmanagement.application.internal.queryservices;

import com.osoterra.ososense.farmmanagement.domain.model.Crop;
import com.osoterra.ososense.farmmanagement.domain.model.CropId;
import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.Farm;
import com.osoterra.ososense.farmmanagement.domain.model.FarmId;
import com.osoterra.ososense.farmmanagement.domain.model.Plot;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.farmmanagement.domain.repositories.CropRepository;
import com.osoterra.ososense.farmmanagement.domain.repositories.DeviceRepository;
import com.osoterra.ososense.farmmanagement.domain.repositories.FarmRepository;
import com.osoterra.ososense.farmmanagement.domain.repositories.PlotRepository;
import com.osoterra.ososense.farmmanagement.domain.services.FarmManagementQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class FarmManagementQueryServiceImpl implements FarmManagementQueryService {

    private final FarmRepository farmRepository;
    private final PlotRepository plotRepository;
    private final DeviceRepository deviceRepository;
    private final CropRepository cropRepository;

    FarmManagementQueryServiceImpl(
            FarmRepository farmRepository,
            PlotRepository plotRepository,
            DeviceRepository deviceRepository,
            CropRepository cropRepository) {
        this.farmRepository = farmRepository;
        this.plotRepository = plotRepository;
        this.deviceRepository = deviceRepository;
        this.cropRepository = cropRepository;
    }

    @Override
    public Optional<Farm> findFarmById(FarmId id) {
        return farmRepository.findById(id);
    }

    @Override
    public List<Farm> findFarmsByOwnerId(Long ownerId) {
        return farmRepository.findByOwnerId(ownerId);
    }

    @Override
    public Optional<Plot> findPlotById(PlotId id) {
        return plotRepository.findById(id);
    }

    @Override
    public List<Plot> findPlotsByFarmId(FarmId farmId) {
        return plotRepository.findByFarmId(farmId);
    }

    @Override
    public Optional<Device> findDeviceById(DeviceId id) {
        return deviceRepository.findById(id);
    }

    @Override
    public Optional<Device> findDeviceByPlotId(PlotId plotId) {
        return deviceRepository.findByPlotId(plotId);
    }

    @Override
    public List<Crop> findAllCrops() {
        return cropRepository.findAll();
    }

    @Override
    public Optional<Crop> findCropById(CropId id) {
        return cropRepository.findById(id);
    }
}
