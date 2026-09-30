package com.osoterra.ososense.salinityalerting.domain.services;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;

import java.util.List;
import java.util.Optional;

public interface SalinityAlertingQueryService {

    Optional<SalinityAlert> findAlertById(SalinityAlertId id);

    List<SalinityAlert> findAlertsByPlotId(Long plotId);

    Optional<CorrectiveAction> findCorrectiveActionByAlertId(Long salinityAlertId);

    Optional<NotificationPreference> findNotificationPreferenceByUserAccountId(Long userAccountId);
}
