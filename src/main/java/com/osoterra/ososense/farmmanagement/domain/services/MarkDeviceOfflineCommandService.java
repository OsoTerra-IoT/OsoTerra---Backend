package com.osoterra.ososense.farmmanagement.domain.services;

import com.osoterra.ososense.farmmanagement.domain.model.Device;

public interface MarkDeviceOfflineCommandService {

    Device handle(MarkDeviceOfflineCommand command);
}
