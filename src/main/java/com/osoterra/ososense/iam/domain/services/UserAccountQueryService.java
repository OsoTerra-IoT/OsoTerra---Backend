package com.osoterra.ososense.iam.domain.services;

import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserAccountId;

import java.util.List;
import java.util.Optional;

public interface UserAccountQueryService {

    Optional<UserAccount> findById(UserAccountId id);

    /**
     * Advisors with an accepted, currently active link to the given farmer.
     */
    List<UserAccount> findAdvisorsLinkedToFarmer(UserAccountId farmerId);

    /**
     * Farmers with an accepted, currently active link to the given advisor.
     */
    List<UserAccount> findFarmersLinkedToAdvisor(UserAccountId advisorId);
}
