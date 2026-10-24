package com.osoterra.ososense.iam.interfaces.rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import com.osoterra.ososense.iam.domain.services.UserAccountQueryService;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.model.AdvisoryLink;
import com.osoterra.ososense.iam.domain.services.AcceptAdvisoryLinkCommand;
import com.osoterra.ososense.iam.domain.services.AcceptAdvisoryLinkCommandService;
import com.osoterra.ososense.iam.domain.services.RequestAdvisoryLinkCommand;
import com.osoterra.ososense.iam.domain.services.RequestAdvisoryLinkCommandService;
import com.osoterra.ososense.iam.domain.services.RevokeAdvisoryLinkCommand;
import com.osoterra.ososense.iam.domain.services.RevokeAdvisoryLinkCommandService;
import com.osoterra.ososense.iam.interfaces.rest.resources.AdvisoryLinkResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.AdvisoryLinkResourceAssembler;
import com.osoterra.ososense.iam.interfaces.rest.resources.RequestAdvisoryLinkResource;
import com.osoterra.ososense.shared.interfaces.rest.CurrentUserId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/advisory-links")
class AdvisoryLinkController {

    private final RequestAdvisoryLinkCommandService requestAdvisoryLinkCommandService;
    private final AcceptAdvisoryLinkCommandService acceptAdvisoryLinkCommandService;
    private final RevokeAdvisoryLinkCommandService revokeAdvisoryLinkCommandService;
    private final AdvisoryLinkResourceAssembler advisoryLinkResourceAssembler;
    private final UserAccountQueryService userAccountQueryService;

    AdvisoryLinkController(
            RequestAdvisoryLinkCommandService requestAdvisoryLinkCommandService,
            AcceptAdvisoryLinkCommandService acceptAdvisoryLinkCommandService,
            RevokeAdvisoryLinkCommandService revokeAdvisoryLinkCommandService,
            AdvisoryLinkResourceAssembler advisoryLinkResourceAssembler,
            UserAccountQueryService userAccountQueryService) {
        this.requestAdvisoryLinkCommandService = requestAdvisoryLinkCommandService;
        this.acceptAdvisoryLinkCommandService = acceptAdvisoryLinkCommandService;
        this.revokeAdvisoryLinkCommandService = revokeAdvisoryLinkCommandService;
        this.advisoryLinkResourceAssembler = advisoryLinkResourceAssembler;
        this.userAccountQueryService = userAccountQueryService;
    }

    @PostMapping
    ResponseEntity<AdvisoryLinkResource> request(
            @Valid @RequestBody RequestAdvisoryLinkResource request, @CurrentUserId Long advisorId) {
        AdvisoryLink link = requestAdvisoryLinkCommandService.handle(
                new RequestAdvisoryLinkCommand(advisorId, request.farmerId(), request.farmerEmail()));

        return ResponseEntity.status(HttpStatus.CREATED).body(advisoryLinkResourceAssembler.toResource(link));
    }

    /**
     * The caller's pending and accepted links, as advisor or as farmer, newest first.
     */
    @GetMapping("/mine")
    List<AdvisoryLinkResource> mine(@CurrentUserId Long userId) {
        return userAccountQueryService.findOpenAdvisoryLinks(new UserAccountId(userId)).stream()
                .map(advisoryLinkResourceAssembler::toResource)
                .toList();
    }

    @PostMapping("/{id}/acceptance")
    ResponseEntity<AdvisoryLinkResource> accept(@PathVariable Long id, @CurrentUserId Long requestingUserId) {
        AdvisoryLink link =
                acceptAdvisoryLinkCommandService.handle(new AcceptAdvisoryLinkCommand(id, requestingUserId));
        return ResponseEntity.ok(advisoryLinkResourceAssembler.toResource(link));
    }

    @PostMapping("/{id}/revocation")
    ResponseEntity<AdvisoryLinkResource> revoke(@PathVariable Long id, @CurrentUserId Long requestingUserId) {
        AdvisoryLink link =
                revokeAdvisoryLinkCommandService.handle(new RevokeAdvisoryLinkCommand(id, requestingUserId));
        return ResponseEntity.ok(advisoryLinkResourceAssembler.toResource(link));
    }
}
