package com.osoterra.ososense.iam.infrastructure.persistence.jpa;

import com.osoterra.ososense.iam.domain.model.AdvisoryLink;
import com.osoterra.ososense.iam.domain.model.AdvisoryLinkId;
import com.osoterra.ososense.iam.domain.repositories.AdvisoryLinkRepository;
import com.osoterra.ososense.iam.domain.model.LinkStatus;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
class JpaAdvisoryLinkRepository implements AdvisoryLinkRepository {

    private static final List<LinkStatus> OPEN_STATUSES = List.of(LinkStatus.PENDING, LinkStatus.ACCEPTED);

    private final SpringDataAdvisoryLinkJpaRepository springDataRepository;

    JpaAdvisoryLinkRepository(SpringDataAdvisoryLinkJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public AdvisoryLink save(AdvisoryLink link) {
        AdvisoryLinkJpaEntity saved = springDataRepository.save(AdvisoryLinkMapper.toEntity(link));
        return AdvisoryLinkMapper.toDomain(saved);
    }

    @Override
    public Optional<AdvisoryLink> findById(AdvisoryLinkId id) {
        return springDataRepository.findById(id.value()).map(AdvisoryLinkMapper::toDomain);
    }

    @Override
    public List<AdvisoryLink> findActiveByFarmerId(UserAccountId farmerId) {
        return springDataRepository.findByFarmerIdAndStatus(farmerId.value(), LinkStatus.ACCEPTED).stream()
                .map(AdvisoryLinkMapper::toDomain)
                .toList();
    }

    @Override
    public List<AdvisoryLink> findActiveByAdvisorId(UserAccountId advisorId) {
        return springDataRepository.findByAdvisorIdAndStatus(advisorId.value(), LinkStatus.ACCEPTED).stream()
                .map(AdvisoryLinkMapper::toDomain)
                .toList();
    }

    @Override
    public List<AdvisoryLink> findOpenByParticipant(UserAccountId userId) {
        // Spring Data binds the status filter to the farmer branch only, so filter both branches here.
        return springDataRepository
                .findByAdvisorIdOrFarmerIdAndStatusIn(userId.value(), userId.value(), OPEN_STATUSES).stream()
                .map(AdvisoryLinkMapper::toDomain)
                .filter(link -> OPEN_STATUSES.contains(link.getStatus()))
                .sorted(Comparator.comparing(AdvisoryLink::getRequestedAt).reversed())
                .toList();
    }
}
