package com.osoterra.ososense.analyticsreporting.domain.services;

import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;

public interface ComputeSalinityTrendCommandService {

    SalinityTrend handle(ComputeSalinityTrendCommand command);
}
