package com.osoterra.ososense.identityaccess.interfaces.rest.controllers;

import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;
import com.osoterra.ososense.identityaccess.domain.services.UserAccountQueryService;
import com.osoterra.ososense.identityaccess.interfaces.rest.resources.UserAccountResource;
import com.osoterra.ososense.identityaccess.interfaces.rest.resources.UserAccountResourceAssembler;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
class UserAccountController {

    private final UserAccountQueryService userAccountQueryService;
    private final UserAccountResourceAssembler userAccountResourceAssembler;

    UserAccountController(
            UserAccountQueryService userAccountQueryService, UserAccountResourceAssembler userAccountResourceAssembler) {
        this.userAccountQueryService = userAccountQueryService;
        this.userAccountResourceAssembler = userAccountResourceAssembler;
    }

    @GetMapping("/me")
    UserAccountResource getCurrentUser(@CurrentUserId UserAccountId id) {
        return userAccountQueryService
                .findById(id)
                .map(userAccountResourceAssembler::toResource)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for id " + id));
    }
}
