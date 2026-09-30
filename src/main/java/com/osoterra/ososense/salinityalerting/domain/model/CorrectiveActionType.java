package com.osoterra.ososense.salinityalerting.domain.model;

/**
 * The closed list of corrective actions a producer can register against an alert. The
 * database column is unconstrained ({@code VARCHAR}, no CHECK), so this enum is the
 * only place the five-value list is enforced.
 */
public enum CorrectiveActionType {
    LEACHING,
    DRAINAGE_IMPROVEMENT,
    IRRIGATION_ADJUSTMENT,
    SOIL_AMENDMENT,
    CROP_ROTATION
}
