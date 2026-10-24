package com.osoterra.ososense.shared.domain.services;

/**
 * Who may see and change a farm's data. A farmer reads and changes their own farms, plots and
 * devices; an advisor with an accepted advisory link to the farmer reads them and acts on them
 * (alerts, calibration, reports) but cannot change the farm's structure. Every check throws
 * {@code ForbiddenOperationException} (403) when the user has no access and
 * {@code EntityNotFoundException} (404) when the resource does not exist.
 *
 * <p>Implemented by Farm Management, which owns farms, plots and devices; the other bounded
 * contexts depend only on this interface.
 */
public interface PlotAccessPolicy {

    void checkFarmRead(Long userId, Long farmId);

    void checkFarmWrite(Long userId, Long farmId);

    void checkPlotRead(Long userId, Long plotId);

    void checkPlotWrite(Long userId, Long plotId);

    /** A device not yet attached to a plot holds no farm data, so any signed-in user may see it. */
    void checkDeviceRead(Long userId, Long deviceId);
}
