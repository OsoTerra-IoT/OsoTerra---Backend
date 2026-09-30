package com.osoterra.ososense.farmmanagement.domain.events;

import com.osoterra.ososense.farmmanagement.domain.model.CropId;
import com.osoterra.ososense.farmmanagement.domain.model.PlotId;
import com.osoterra.ososense.shared.domain.events.DomainEvent;

import java.time.Instant;

public record CropAssignedToPlotEvent(PlotId plotId, CropId cropId, Instant occurredOn) implements DomainEvent {
}
