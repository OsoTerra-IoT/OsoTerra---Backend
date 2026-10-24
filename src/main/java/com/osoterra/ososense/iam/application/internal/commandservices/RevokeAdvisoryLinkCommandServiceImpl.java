package com.osoterra.ososense.iam.application.internal.commandservices;

import com.osoterra.ososense.iam.domain.model.AdvisoryLink;
import com.osoterra.ososense.iam.domain.model.AdvisoryLinkId;
import com.osoterra.ososense.iam.domain.repositories.AdvisoryLinkRepository;
import com.osoterra.ososense.iam.domain.services.RevokeAdvisoryLinkCommand;
import com.osoterra.ososense.iam.domain.services.RevokeAdvisoryLinkCommandService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import com.osoterra.ososense.shared.domain.exceptions.ForbiddenOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class RevokeAdvisoryLinkCommandServiceImpl implements RevokeAdvisoryLinkCommandService {

    private final AdvisoryLinkRepository advisoryLinkRepository;

    RevokeAdvisoryLinkCommandServiceImpl(AdvisoryLinkRepository advisoryLinkRepository) {
        this.advisoryLinkRepository = advisoryLinkRepository;
    }

    @Override
    public AdvisoryLink handle(RevokeAdvisoryLinkCommand command) {
        AdvisoryLinkId id = new AdvisoryLinkId(command.linkId());
        AdvisoryLink link = advisoryLinkRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advisory link not found for id " + id));

        boolean isParty = link.getAdvisorId().value().equals(command.requestingUserId())
                || link.getFarmerId().value().equals(command.requestingUserId());
        if (!isParty) {
            throw new ForbiddenOperationException("You are not part of this advisory link");
        }

        link.revoke();
        return advisoryLinkRepository.save(link);
    }
}
