package com.osoterra.ososense.farmmanagement.domain.model;

import com.osoterra.ososense.farmmanagement.domain.events.DeviceInstalledInPlotEvent;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeviceTest {

    @Test
    void registerWithActivationCodeStartsUnassigned() {
        Device device = Device.registerWithActivationCode("ACT-0001");

        assertThat(device.getStatus()).isEqualTo(DeviceStatus.UNASSIGNED);
        assertThat(device.getPlotId()).isEmpty();
    }

    @Test
    void attachToPlotActivatesTheDeviceAndPublishesDeviceInstalledInPlotEvent() {
        Device device = Device.registerWithActivationCode("ACT-0002");
        PlotId plotId = new PlotId(7L);

        device.attachToPlot(plotId);

        assertThat(device.getStatus()).isEqualTo(DeviceStatus.ACTIVE);
        assertThat(device.getPlotId()).contains(plotId);
        var events = device.pullDomainEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOfSatisfying(
                DeviceInstalledInPlotEvent.class, event -> assertThat(event.plotId()).isEqualTo(plotId));
    }

    @Test
    void attachToPlotRejectsADeviceThatIsAlreadyAssigned() {
        Device device = Device.registerWithActivationCode("ACT-0003");
        device.attachToPlot(new PlotId(1L));

        assertThatThrownBy(() -> device.attachToPlot(new PlotId(2L)))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void markOfflineTransitionsAnActiveDeviceToOffline() {
        Device device = Device.registerWithActivationCode("ACT-0004");
        device.attachToPlot(new PlotId(1L));

        device.markOffline();

        assertThat(device.getStatus()).isEqualTo(DeviceStatus.OFFLINE);
    }

    @Test
    void markOfflineDoesNothingWhenTheDeviceIsNotActive() {
        Device device = Device.registerWithActivationCode("ACT-0005");

        device.markOffline();

        assertThat(device.getStatus()).isEqualTo(DeviceStatus.UNASSIGNED);
    }
}
