package com.osoterra.ososense.farmmanagement.domain.repositories;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;

import java.util.Optional;

public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findById(DeviceId id);

    Optional<Device> findByActivationCode(String activationCode);

    Optional<Device> findByPlotId(PlotId plotId);

    boolean existsByActivationCode(String activationCode);
}
