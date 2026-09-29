package com.osoterra.ososense.farmmanagement.domain.events;

import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.shared.DomainEvent;

import java.time.Instant;

public record DeviceInstalledInPlotEvent(DeviceId deviceId, PlotId plotId, Instant occurredOn) implements DomainEvent {
}
