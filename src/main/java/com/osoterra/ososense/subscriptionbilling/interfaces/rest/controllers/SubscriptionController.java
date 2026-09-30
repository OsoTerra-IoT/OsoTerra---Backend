package com.osoterra.ososense.subscriptionbilling.interfaces.rest.controllers;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.services.CancelSubscriptionCommand;
import com.osoterra.ososense.subscriptionbilling.domain.services.CancelSubscriptionCommandService;
import com.osoterra.ososense.subscriptionbilling.domain.services.RequestSubscriptionCommand;
import com.osoterra.ososense.subscriptionbilling.domain.services.RequestSubscriptionCommandService;
import com.osoterra.ososense.subscriptionbilling.domain.services.SubscriptionBillingQueryService;
import com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources.RequestSubscriptionResource;
import com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources.SubscriptionResource;
import com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources.SubscriptionResourceAssembler;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import com.osoterra.ososense.shared.interfaces.rest.CurrentUserId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
class SubscriptionController {

    private final RequestSubscriptionCommandService requestSubscriptionCommandService;
    private final CancelSubscriptionCommandService cancelSubscriptionCommandService;
    private final SubscriptionBillingQueryService subscriptionBillingQueryService;
    private final SubscriptionResourceAssembler subscriptionResourceAssembler;

    SubscriptionController(
            RequestSubscriptionCommandService requestSubscriptionCommandService,
            CancelSubscriptionCommandService cancelSubscriptionCommandService,
            SubscriptionBillingQueryService subscriptionBillingQueryService,
            SubscriptionResourceAssembler subscriptionResourceAssembler) {
        this.requestSubscriptionCommandService = requestSubscriptionCommandService;
        this.cancelSubscriptionCommandService = cancelSubscriptionCommandService;
        this.subscriptionBillingQueryService = subscriptionBillingQueryService;
        this.subscriptionResourceAssembler = subscriptionResourceAssembler;
    }

    @PostMapping
    ResponseEntity<SubscriptionResource> request(
            @Valid @RequestBody RequestSubscriptionResource request, @CurrentUserId Long userAccountId) {
        Subscription subscription = requestSubscriptionCommandService.handle(
                new RequestSubscriptionCommand(userAccountId, request.subscriptionPlanId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionResourceAssembler.toResource(subscription));
    }

    @PostMapping("/{id}/cancellation")
    ResponseEntity<SubscriptionResource> cancel(@PathVariable Long id) {
        Subscription subscription = cancelSubscriptionCommandService.handle(new CancelSubscriptionCommand(id));
        return ResponseEntity.ok(subscriptionResourceAssembler.toResource(subscription));
    }

    @GetMapping("/mine")
    List<SubscriptionResource> mine(@CurrentUserId Long userAccountId) {
        return subscriptionBillingQueryService.findSubscriptionsByUserAccountId(userAccountId).stream()
                .map(subscriptionResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/{id}")
    SubscriptionResource getById(@PathVariable Long id) {
        return subscriptionBillingQueryService
                .findSubscriptionById(new SubscriptionId(id))
                .map(subscriptionResourceAssembler::toResource)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id " + id));
    }
}
