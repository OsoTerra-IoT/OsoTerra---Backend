package com.osoterra.ososense.salinityalerting.interfaces.rest.controllers;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;
import com.osoterra.ososense.salinityalerting.domain.services.AcknowledgeAlertCommand;
import com.osoterra.ososense.salinityalerting.domain.services.AcknowledgeAlertCommandService;
import com.osoterra.ososense.salinityalerting.domain.services.RegisterCorrectiveActionCommand;
import com.osoterra.ososense.salinityalerting.domain.services.RegisterCorrectiveActionCommandService;
import com.osoterra.ososense.salinityalerting.domain.services.SalinityAlertingQueryService;
import com.osoterra.ososense.salinityalerting.interfaces.rest.resources.CorrectiveActionResource;
import com.osoterra.ososense.salinityalerting.interfaces.rest.resources.CorrectiveActionResourceAssembler;
import com.osoterra.ososense.salinityalerting.interfaces.rest.resources.RegisterCorrectiveActionResource;
import com.osoterra.ososense.salinityalerting.interfaces.rest.resources.SalinityAlertResource;
import com.osoterra.ososense.salinityalerting.interfaces.rest.resources.SalinityAlertResourceAssembler;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import com.osoterra.ososense.shared.interfaces.rest.CurrentUserId;
import com.osoterra.ososense.shared.domain.services.PlotAccessPolicy;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salinity-alerts")
class SalinityAlertController {

    private final AcknowledgeAlertCommandService acknowledgeAlertCommandService;
    private final RegisterCorrectiveActionCommandService registerCorrectiveActionCommandService;
    private final SalinityAlertingQueryService salinityAlertingQueryService;
    private final SalinityAlertResourceAssembler salinityAlertResourceAssembler;
    private final CorrectiveActionResourceAssembler correctiveActionResourceAssembler;
    private final PlotAccessPolicy plotAccessPolicy;

    SalinityAlertController(
            AcknowledgeAlertCommandService acknowledgeAlertCommandService,
            RegisterCorrectiveActionCommandService registerCorrectiveActionCommandService,
            SalinityAlertingQueryService salinityAlertingQueryService,
            SalinityAlertResourceAssembler salinityAlertResourceAssembler,
            CorrectiveActionResourceAssembler correctiveActionResourceAssembler,
            PlotAccessPolicy plotAccessPolicy) {
        this.acknowledgeAlertCommandService = acknowledgeAlertCommandService;
        this.registerCorrectiveActionCommandService = registerCorrectiveActionCommandService;
        this.salinityAlertingQueryService = salinityAlertingQueryService;
        this.salinityAlertResourceAssembler = salinityAlertResourceAssembler;
        this.correctiveActionResourceAssembler = correctiveActionResourceAssembler;
        this.plotAccessPolicy = plotAccessPolicy;
    }

    @GetMapping
    List<SalinityAlertResource> byPlot(@RequestParam Long plotId, @CurrentUserId Long userId) {
        plotAccessPolicy.checkPlotRead(userId, plotId);
        return salinityAlertingQueryService.findAlertsByPlotId(plotId).stream()
                .map(salinityAlertResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/{id}")
    SalinityAlertResource getById(@PathVariable Long id, @CurrentUserId Long userId) {
        return salinityAlertResourceAssembler.toResource(readableAlert(id, userId));
    }

    @PostMapping("/{id}/acknowledgement")
    ResponseEntity<SalinityAlertResource> acknowledge(@PathVariable Long id, @CurrentUserId Long userId) {
        readableAlert(id, userId);
        SalinityAlert alert = acknowledgeAlertCommandService.handle(new AcknowledgeAlertCommand(id, userId));
        return ResponseEntity.ok(salinityAlertResourceAssembler.toResource(alert));
    }

    @PostMapping("/{id}/corrective-actions")
    ResponseEntity<CorrectiveActionResource> registerCorrectiveAction(
            @PathVariable Long id, @Valid @RequestBody RegisterCorrectiveActionResource request,
            @CurrentUserId Long userId) {
        readableAlert(id, userId);
        CorrectiveAction action = registerCorrectiveActionCommandService.handle(new RegisterCorrectiveActionCommand(
                id, request.actionType(), request.executedAt(), request.notes(), userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(correctiveActionResourceAssembler.toResource(action));
    }

    /**
     * Corrective actions registered for an alert: empty while it is unresolved, since
     * registering one resolves the alert.
     */
    @GetMapping("/{id}/corrective-actions")
    List<CorrectiveActionResource> correctiveActions(@PathVariable Long id, @CurrentUserId Long userId) {
        readableAlert(id, userId);
        return salinityAlertingQueryService.findCorrectiveActionByAlertId(id)
                .map(correctiveActionResourceAssembler::toResource)
                .stream()
                .toList();
    }

    /** The alert, if the user may see its plot: its farmer or an advisor linked to them. */
    private SalinityAlert readableAlert(Long id, Long userId) {
        SalinityAlert alert = salinityAlertingQueryService
                .findAlertById(new SalinityAlertId(id))
                .orElseThrow(() -> new EntityNotFoundException("Salinity alert not found for id " + id));
        plotAccessPolicy.checkPlotRead(userId, alert.getPlotId());
        return alert;
    }
}
