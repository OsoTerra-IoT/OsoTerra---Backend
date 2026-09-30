package com.osoterra.ososense.iam.interfaces.rest.controllers;

import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.services.UserAccountQueryService;
import com.osoterra.ososense.iam.interfaces.rest.resources.UserAccountResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.UserAccountResourceAssembler;
import com.osoterra.ososense.shared.EntityNotFoundException;
import com.osoterra.ososense.shared.web.CurrentUserId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * Accounts linked to the current user through an accepted advisory link: the farmers
     * an advisor works with, or the advisors of a farmer.
     */
    @GetMapping("/me/linked-accounts")
    List<UserAccountResource> linkedAccounts(@CurrentUserId Long id) {
        UserAccount account = userAccountQueryService
                .findById(new UserAccountId(id))
                .orElseThrow(() -> new EntityNotFoundException("Account not found for id " + id));
        List<UserAccount> linked = account.isAdvisor()
                ? userAccountQueryService.findFarmersLinkedToAdvisor(account.getId())
                : userAccountQueryService.findAdvisorsLinkedToFarmer(account.getId());
        return linked.stream().map(userAccountResourceAssembler::toResource).toList();
    }
}
