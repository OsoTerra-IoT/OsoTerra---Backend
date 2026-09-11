package com.osoterra.ososense.identityaccess.infrastructure.persistence.jpa;

import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLink;
import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLinkId;
import com.osoterra.ososense.identityaccess.domain.repositories.AdvisoryLinkRepository;
import com.osoterra.ososense.identityaccess.domain.model.LinkStatus;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaAdvisoryLinkRepository implements AdvisoryLinkRepository {

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
}
