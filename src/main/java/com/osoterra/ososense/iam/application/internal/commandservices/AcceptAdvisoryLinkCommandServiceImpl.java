package com.osoterra.ososense.iam.application.internal.commandservices;

import com.osoterra.ososense.iam.domain.model.AdvisoryLink;
import com.osoterra.ososense.iam.domain.model.AdvisoryLinkId;
import com.osoterra.ososense.iam.domain.repositories.AdvisoryLinkRepository;
import com.osoterra.ososense.iam.domain.services.AcceptAdvisoryLinkCommand;
import com.osoterra.ososense.iam.domain.services.AcceptAdvisoryLinkCommandService;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class AcceptAdvisoryLinkCommandServiceImpl implements AcceptAdvisoryLinkCommandService {

    private final AdvisoryLinkRepository advisoryLinkRepository;

    AcceptAdvisoryLinkCommandServiceImpl(AdvisoryLinkRepository advisoryLinkRepository) {
        this.advisoryLinkRepository = advisoryLinkRepository;
    }

    @Override
    public AdvisoryLink handle(AcceptAdvisoryLinkCommand command) {
        AdvisoryLinkId id = new AdvisoryLinkId(command.linkId());
        AdvisoryLink link = advisoryLinkRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advisory link not found for id " + id));

        if (!link.getFarmerId().value().equals(command.requestingUserId())) {
            throw new BusinessRuleViolationException("Only the farmer can accept this advisory link");
        }

        link.accept();
        return advisoryLinkRepository.save(link);
    }
}
