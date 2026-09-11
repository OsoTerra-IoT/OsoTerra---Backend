package com.osoterra.ososense.identityaccess.interfaces.rest.controllers;

import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLink;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;
import com.osoterra.ososense.identityaccess.domain.services.AcceptAdvisoryLinkCommand;
import com.osoterra.ososense.identityaccess.domain.services.AcceptAdvisoryLinkCommandService;
import com.osoterra.ososense.identityaccess.domain.services.RequestAdvisoryLinkCommand;
import com.osoterra.ososense.identityaccess.domain.services.RequestAdvisoryLinkCommandService;
import com.osoterra.ososense.identityaccess.domain.services.RevokeAdvisoryLinkCommand;
import com.osoterra.ososense.identityaccess.domain.services.RevokeAdvisoryLinkCommandService;
import com.osoterra.ososense.identityaccess.interfaces.rest.resources.AdvisoryLinkResource;
import com.osoterra.ososense.identityaccess.interfaces.rest.resources.AdvisoryLinkResourceAssembler;
import com.osoterra.ososense.identityaccess.interfaces.rest.resources.RequestAdvisoryLinkResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/advisory-links")
class AdvisoryLinkController {

    private final RequestAdvisoryLinkCommandService requestAdvisoryLinkCommandService;
    private final AcceptAdvisoryLinkCommandService acceptAdvisoryLinkCommandService;
    private final RevokeAdvisoryLinkCommandService revokeAdvisoryLinkCommandService;
    private final AdvisoryLinkResourceAssembler advisoryLinkResourceAssembler;

    AdvisoryLinkController(
            RequestAdvisoryLinkCommandService requestAdvisoryLinkCommandService,
            AcceptAdvisoryLinkCommandService acceptAdvisoryLinkCommandService,
            RevokeAdvisoryLinkCommandService revokeAdvisoryLinkCommandService,
            AdvisoryLinkResourceAssembler advisoryLinkResourceAssembler) {
        this.requestAdvisoryLinkCommandService = requestAdvisoryLinkCommandService;
        this.acceptAdvisoryLinkCommandService = acceptAdvisoryLinkCommandService;
        this.revokeAdvisoryLinkCommandService = revokeAdvisoryLinkCommandService;
        this.advisoryLinkResourceAssembler = advisoryLinkResourceAssembler;
    }

    @PostMapping
    ResponseEntity<AdvisoryLinkResource> request(
            @Valid @RequestBody RequestAdvisoryLinkResource request, @CurrentUserId UserAccountId advisorId) {
        AdvisoryLink link = requestAdvisoryLinkCommandService.handle(
                new RequestAdvisoryLinkCommand(advisorId.value(), request.farmerId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(advisoryLinkResourceAssembler.toResource(link));
    }

    @PostMapping("/{id}/acceptance")
    ResponseEntity<AdvisoryLinkResource> accept(@PathVariable Long id, @CurrentUserId UserAccountId requestingUserId) {
        AdvisoryLink link =
                acceptAdvisoryLinkCommandService.handle(new AcceptAdvisoryLinkCommand(id, requestingUserId.value()));
        return ResponseEntity.ok(advisoryLinkResourceAssembler.toResource(link));
    }

    @PostMapping("/{id}/revocation")
    ResponseEntity<AdvisoryLinkResource> revoke(@PathVariable Long id, @CurrentUserId UserAccountId requestingUserId) {
        AdvisoryLink link =
                revokeAdvisoryLinkCommandService.handle(new RevokeAdvisoryLinkCommand(id, requestingUserId.value()));
        return ResponseEntity.ok(advisoryLinkResourceAssembler.toResource(link));
    }
}
