package com.osoterra.ososense.salinityalerting.application.internal.commandservices;

import com.osoterra.ososense.salinityalerting.domain.gateways.CropThresholdLookup;
import com.osoterra.ososense.salinityalerting.domain.gateways.NotificationDispatcher;
import com.osoterra.ososense.salinityalerting.domain.gateways.PlotOwnerLookup;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationChannel;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.repositories.NotificationPreferenceRepository;
import com.osoterra.ososense.salinityalerting.domain.repositories.SalinityAlertRepository;
import com.osoterra.ososense.salinityalerting.domain.services.EvaluateReadingCommand;
import com.osoterra.ososense.salinityalerting.domain.services.EvaluateReadingCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Evaluates one soil reading and, if it exceeds the plot's crop threshold and the plot's
 * latest alert does not already cover it, generates a {@code SalinityAlert} and dispatches
 * a notification per the plot owner's preferences. The latest alert covers the reading while
 * it is open or acknowledged (not yet resolved) and the new severity is equal or lower; a
 * higher severity, or a resolved latest alert, raises a new one.
 */
@Service
@Transactional
class EvaluateReadingCommandServiceImpl implements EvaluateReadingCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluateReadingCommandServiceImpl.class);

    private final CropThresholdLookup cropThresholdLookup;
    private final PlotOwnerLookup plotOwnerLookup;
    private final SalinityAlertRepository salinityAlertRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final List<NotificationDispatcher> notificationDispatchers;

    EvaluateReadingCommandServiceImpl(
            CropThresholdLookup cropThresholdLookup,
            PlotOwnerLookup plotOwnerLookup,
            SalinityAlertRepository salinityAlertRepository,
            NotificationPreferenceRepository notificationPreferenceRepository,
            List<NotificationDispatcher> notificationDispatchers) {
        this.cropThresholdLookup = cropThresholdLookup;
        this.plotOwnerLookup = plotOwnerLookup;
        this.salinityAlertRepository = salinityAlertRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.notificationDispatchers = notificationDispatchers;
    }

    @Override
    public void handle(EvaluateReadingCommand command) {
        Optional<BigDecimal> threshold = cropThresholdLookup.findThresholdForPlot(command.plotId());
        if (threshold.isEmpty() || command.observedConductivityDsM().compareTo(threshold.get()) <= 0) {
            return;
        }

        SalinityAlert candidate = SalinityAlert.generate(
                command.plotId(), command.soilReadingId(), command.observedConductivityDsM(), threshold.get());

        boolean alreadyCovered = salinityAlertRepository.findLatestByPlotId(command.plotId())
                .map(latest -> latest.stillCovers(candidate.getSeverity()))
                .orElse(false);
        if (alreadyCovered) {
            return;
        }

        SalinityAlert alert = salinityAlertRepository.save(candidate);
        notifyPlotOwner(alert);
    }

    private void notifyPlotOwner(SalinityAlert alert) {
        Long ownerId = plotOwnerLookup.findOwnerIdForPlot(alert.getPlotId()).orElse(null);
        if (ownerId == null) {
            return;
        }
        NotificationPreference preference = notificationPreferenceRepository
                .findByUserAccountId(ownerId)
                .orElseGet(() -> NotificationPreference.defaultFor(ownerId));
        if (!preference.isSatisfiedBy(alert.getSeverity())) {
            return;
        }
        for (NotificationDispatcher dispatcher : notificationDispatchers) {
            if (preference.getChannel() == NotificationChannel.BOTH
                    || preference.getChannel() == dispatcher.supportedChannel()) {
                dispatchSafely(dispatcher, alert, ownerId, preference.getPushDeviceToken().orElse(null));
            }
        }
    }

    /**
     * Delivery is best effort: the alert is already stored and visible in the apps, so a
     * mail or push outage must not reject the telemetry batch that raised it.
     */
    private void dispatchSafely(NotificationDispatcher dispatcher, SalinityAlert alert, Long ownerId, String pushToken) {
        try {
            dispatcher.dispatch(alert, ownerId, pushToken);
        } catch (RuntimeException ex) {
            LOGGER.warn("Could not deliver alert {} via {}: {}", alert.getId(), dispatcher.supportedChannel(), ex.getMessage());
        }
    }
}
