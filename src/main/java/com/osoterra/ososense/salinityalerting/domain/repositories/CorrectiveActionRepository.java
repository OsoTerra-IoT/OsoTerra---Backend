package com.osoterra.ososense.salinityalerting.domain.repositories;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;

import java.util.Optional;

public interface CorrectiveActionRepository {

    CorrectiveAction save(CorrectiveAction action);

    Optional<CorrectiveAction> findBySalinityAlertId(Long salinityAlertId);
}
