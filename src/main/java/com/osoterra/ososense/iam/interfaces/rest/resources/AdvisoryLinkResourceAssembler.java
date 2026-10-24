package com.osoterra.ososense.iam.interfaces.rest.resources;

import com.osoterra.ososense.iam.domain.model.AdvisoryLink;
import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.services.UserAccountQueryService;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Builds the link resource with both people's names and emails, so each side knows who the
 * other one is before accepting or ending the supervision.
 */
@Component
public class AdvisoryLinkResourceAssembler {

    private final UserAccountQueryService userAccountQueryService;

    AdvisoryLinkResourceAssembler(UserAccountQueryService userAccountQueryService) {
        this.userAccountQueryService = userAccountQueryService;
    }

    public AdvisoryLinkResource toResource(AdvisoryLink link) {
        Optional<UserAccount> advisor = account(link.getAdvisorId());
        Optional<UserAccount> farmer = account(link.getFarmerId());
        return new AdvisoryLinkResource(
                link.getId().value(),
                link.getAdvisorId().value(),
                link.getFarmerId().value(),
                link.getStatus().name(),
                link.getRequestedAt(),
                link.getRespondedAt().orElse(null),
                advisor.map(account -> account.getName().getFullName()).orElse(null),
                advisor.map(account -> account.getEmail().value()).orElse(null),
                farmer.map(account -> account.getName().getFullName()).orElse(null),
                farmer.map(account -> account.getEmail().value()).orElse(null));
    }

    private Optional<UserAccount> account(UserAccountId id) {
        return userAccountQueryService.findById(id);
    }
}
