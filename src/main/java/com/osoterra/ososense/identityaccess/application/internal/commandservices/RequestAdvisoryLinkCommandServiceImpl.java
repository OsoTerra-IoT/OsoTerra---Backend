package com.osoterra.ososense.identityaccess.application.internal.commandservices;

import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLink;
import com.osoterra.ososense.identityaccess.domain.model.UserAccount;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;
import com.osoterra.ososense.identityaccess.domain.repositories.AdvisoryLinkRepository;
import com.osoterra.ososense.identityaccess.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.identityaccess.domain.services.RequestAdvisoryLinkCommand;
import com.osoterra.ososense.identityaccess.domain.services.RequestAdvisoryLinkCommandService;
import com.osoterra.ososense.shared.BusinessRuleViolationException;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class RequestAdvisoryLinkCommandServiceImpl implements RequestAdvisoryLinkCommandService {

    private final AdvisoryLinkRepository advisoryLinkRepository;
    private final UserAccountRepository userAccountRepository;

    RequestAdvisoryLinkCommandServiceImpl(
            AdvisoryLinkRepository advisoryLinkRepository, UserAccountRepository userAccountRepository) {
        this.advisoryLinkRepository = advisoryLinkRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public AdvisoryLink handle(RequestAdvisoryLinkCommand command) {
        UserAccountId advisorId = new UserAccountId(command.advisorId());
        UserAccountId farmerId = new UserAccountId(command.farmerId());

        UserAccount advisor = userAccountRepository
                .findById(advisorId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for id " + advisorId));
        UserAccount farmer = userAccountRepository
                .findById(farmerId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for id " + farmerId));

        if (!advisor.isAdvisor()) {
            throw new BusinessRuleViolationException("Only an advisor account can request a supervision link");
        }
        if (farmer.isAdvisor()) {
            throw new BusinessRuleViolationException("A supervision link must target a farmer account");
        }

        return advisoryLinkRepository.save(AdvisoryLink.request(advisorId, farmerId));
    }
}
