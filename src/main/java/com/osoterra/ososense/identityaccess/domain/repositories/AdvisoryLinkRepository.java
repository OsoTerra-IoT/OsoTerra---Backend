package com.osoterra.ososense.identityaccess.domain.repositories;

import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLink;
import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLinkId;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;

import java.util.List;
import java.util.Optional;

public interface AdvisoryLinkRepository {

    AdvisoryLink save(AdvisoryLink link);

    Optional<AdvisoryLink> findById(AdvisoryLinkId id);

    List<AdvisoryLink> findActiveByFarmerId(UserAccountId farmerId);

    List<AdvisoryLink> findActiveByAdvisorId(UserAccountId advisorId);
}
