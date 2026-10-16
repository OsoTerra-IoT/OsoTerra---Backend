package com.osoterra.ososense.farmmanagement.application.acl;

import com.osoterra.ososense.farmmanagement.domain.gateways.AdvisoryAccessLookup;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.services.UserAccountQueryService;
import org.springframework.stereotype.Component;

/**
 * Anti-corruption layer resolving advisory links through IAM's query service.
 */
@Component
class IamAdvisoryAccessClient implements AdvisoryAccessLookup {

    private final UserAccountQueryService userAccountQueryService;

    IamAdvisoryAccessClient(UserAccountQueryService userAccountQueryService) {
        this.userAccountQueryService = userAccountQueryService;
    }

    @Override
    public boolean isLinked(Long advisorId, Long farmerId) {
        return userAccountQueryService.findFarmersLinkedToAdvisor(new UserAccountId(advisorId)).stream()
                .anyMatch(farmer -> farmer.getId().value().equals(farmerId));
    }
}
