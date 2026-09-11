package com.osoterra.ososense.identityaccess.infrastructure.persistence.jpa;

import com.osoterra.ososense.identityaccess.domain.model.LinkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataAdvisoryLinkJpaRepository extends JpaRepository<AdvisoryLinkJpaEntity, Long> {

    List<AdvisoryLinkJpaEntity> findByFarmerIdAndStatus(Long farmerId, LinkStatus status);

    List<AdvisoryLinkJpaEntity> findByAdvisorIdAndStatus(Long advisorId, LinkStatus status);
}
