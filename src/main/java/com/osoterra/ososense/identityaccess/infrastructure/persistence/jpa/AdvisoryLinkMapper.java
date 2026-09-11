package com.osoterra.ososense.identityaccess.infrastructure.persistence.jpa;

import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLink;
import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLinkId;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;

final class AdvisoryLinkMapper {

    private AdvisoryLinkMapper() {
    }

    static AdvisoryLink toDomain(AdvisoryLinkJpaEntity entity) {
        return AdvisoryLink.reconstruct(
                new AdvisoryLinkId(entity.getId()),
                new UserAccountId(entity.getAdvisorId()),
                new UserAccountId(entity.getFarmerId()),
                entity.getStatus(),
                entity.getRequestedAt(),
                entity.getRespondedAt());
    }

    static AdvisoryLinkJpaEntity toEntity(AdvisoryLink link) {
        Long id = link.getId() == null ? null : link.getId().value();
        return new AdvisoryLinkJpaEntity(
                id,
                link.getAdvisorId().value(),
                link.getFarmerId().value(),
                link.getStatus(),
                link.getRequestedAt(),
                link.getRespondedAt().orElse(null));
    }
}
