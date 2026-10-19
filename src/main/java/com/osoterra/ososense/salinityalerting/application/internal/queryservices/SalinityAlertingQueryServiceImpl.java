package com.osoterra.ososense.salinityalerting.application.internal.queryservices;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;
import com.osoterra.ososense.salinityalerting.domain.repositories.CorrectiveActionRepository;
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
    private final CorrectiveActionRepository correctiveActionRepository;

    SalinityAlertingQueryServiceImpl(
            SalinityAlertRepository salinityAlertRepository,
            NotificationPreferenceRepository notificationPreferenceRepository,
            CorrectiveActionRepository correctiveActionRepository) {
        this.salinityAlertRepository = salinityAlertRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.correctiveActionRepository = correctiveActionRepository;
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
    public Optional<CorrectiveAction> findCorrectiveActionByAlertId(Long salinityAlertId) {
        return correctiveActionRepository.findBySalinityAlertId(salinityAlertId);
    }

    @Override
    public Optional<NotificationPreference> findNotificationPreferenceByUserAccountId(Long userAccountId) {
        return notificationPreferenceRepository.findByUserAccountId(userAccountId);
    }
}
