package com.osoterra.ososense.iam.application.internal.commandservices;

import com.osoterra.ososense.iam.domain.model.AdvisoryLink;
import com.osoterra.ososense.iam.domain.model.EmailAddress;
import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.repositories.AdvisoryLinkRepository;
import com.osoterra.ososense.iam.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.iam.domain.services.RequestAdvisoryLinkCommand;
import com.osoterra.ososense.iam.domain.services.RequestAdvisoryLinkCommandService;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

        UserAccount advisor = userAccountRepository
                .findById(advisorId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for id " + advisorId));
        UserAccount farmer = findFarmer(command);
        UserAccountId farmerId = farmer.getId();

        if (!advisor.isAdvisor()) {
            throw new BusinessRuleViolationException("Only an advisor account can request a supervision link");
        }
        if (farmer.isAdvisor()) {
            throw new BusinessRuleViolationException("A supervision link must target a farmer account");
        }

        boolean alreadyLinked = advisoryLinkRepository.findOpenByParticipant(advisorId).stream()
                .anyMatch(link -> link.getFarmerId().equals(farmerId));
        if (alreadyLinked) {
            throw new BusinessRuleViolationException("A pending or accepted link with this farmer already exists");
        }
        return advisoryLinkRepository.save(AdvisoryLink.request(advisorId, farmerId));
    }

    private UserAccount findFarmer(RequestAdvisoryLinkCommand command) {
        if (command.farmerId() != null) {
            return userAccountRepository
                    .findById(new UserAccountId(command.farmerId()))
                    .orElseThrow(() -> new EntityNotFoundException("Account not found for id " + command.farmerId()));
        }
        EmailAddress email = new EmailAddress(command.farmerEmail().trim());
        return userAccountRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No account with email " + email.value()));
    }
}
