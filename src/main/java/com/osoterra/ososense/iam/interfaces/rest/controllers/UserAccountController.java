package com.osoterra.ososense.iam.interfaces.rest.controllers;

import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.services.UserAccountQueryService;
import com.osoterra.ososense.iam.interfaces.rest.resources.UserAccountResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.UserAccountResourceAssembler;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import com.osoterra.ososense.shared.interfaces.rest.CurrentUserId;
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
    UserAccountResource getCurrentUser(@CurrentUserId Long id) {
        return userAccountQueryService
                .findById(new UserAccountId(id))
                .map(userAccountResourceAssembler::toResource)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for id " + id));
    }
}
