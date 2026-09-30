package com.osoterra.ososense.salinityalerting.application.internal.queryservices;

import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;
import com.osoterra.ososense.salinityalerting.domain.repositories.NotificationPreferenceRepository;
import com.osoterra.ososense.salinityalerting.domain.repositories.SalinityAlertRepository;
import com.osoterra.ososense.salinityalerting.domain.services.SalinityAlertingQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class SalinityAlertingQueryServiceImpl implements SalinityAlertingQueryService {

    private final SalinityAlertRepository salinityAlertRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;

    SalinityAlertingQueryServiceImpl(
            SalinityAlertRepository salinityAlertRepository,
            NotificationPreferenceRepository notificationPreferenceRepository) {
        this.salinityAlertRepository = salinityAlertRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
    }

    @Override
    public Optional<SalinityAlert> findAlertById(SalinityAlertId id) {
        return salinityAlertRepository.findById(id);
    }

    @Override
    public List<SalinityAlert> findAlertsByPlotId(Long plotId) {
        return salinityAlertRepository.findByPlotId(plotId);
    }

    @Override
    public Optional<NotificationPreference> findNotificationPreferenceByUserAccountId(Long userAccountId) {
        return notificationPreferenceRepository.findByUserAccountId(userAccountId);
    }
}
