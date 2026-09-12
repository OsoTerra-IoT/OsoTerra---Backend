package com.osoterra.ososense.iam.application.internal.queryservices;

import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.repositories.AdvisoryLinkRepository;
import com.osoterra.ososense.iam.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.iam.domain.services.UserAccountQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class UserAccountQueryServiceImpl implements UserAccountQueryService {

    private final UserAccountRepository userAccountRepository;
    private final AdvisoryLinkRepository advisoryLinkRepository;

    UserAccountQueryServiceImpl(
            UserAccountRepository userAccountRepository, AdvisoryLinkRepository advisoryLinkRepository) {
        this.userAccountRepository = userAccountRepository;
        this.advisoryLinkRepository = advisoryLinkRepository;
    }

    @Override
    public Optional<UserAccount> findById(UserAccountId id) {
        return userAccountRepository.findById(id);
    }

    @Override
    public List<UserAccount> findAdvisorsLinkedToFarmer(UserAccountId farmerId) {
        return advisoryLinkRepository.findActiveByFarmerId(farmerId).stream()
                .map(link -> userAccountRepository.findById(link.getAdvisorId()))
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public List<UserAccount> findFarmersLinkedToAdvisor(UserAccountId advisorId) {
        return advisoryLinkRepository.findActiveByAdvisorId(advisorId).stream()
                .map(link -> userAccountRepository.findById(link.getFarmerId()))
                .flatMap(Optional::stream)
                .toList();
    }
}
