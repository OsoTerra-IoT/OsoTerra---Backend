package com.osoterra.ososense.salinityalerting.domain.services;

/**
 * Evaluates one stored soil reading against the plot's crop salinity threshold and, if
 * warranted, generates a new alert and dispatches a notification. Called synchronously
 * by Soil Monitoring right after a reading is persisted — the direct in-process
 * substitute for the event-driven policy described in the architecture, since this
 * codebase does not yet publish domain events across module boundaries.
 */
public interface EvaluateReadingCommandService {

    void handle(EvaluateReadingCommand command);
}
