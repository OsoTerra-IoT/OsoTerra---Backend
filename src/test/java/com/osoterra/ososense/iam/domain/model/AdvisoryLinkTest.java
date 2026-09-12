package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.iam.domain.events.AdvisoryLinkAcceptedEvent;
import com.osoterra.ososense.iam.domain.events.AdvisoryLinkRevokedEvent;
import com.osoterra.ososense.shared.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdvisoryLinkTest {

    private static final UserAccountId ADVISOR_ID = new UserAccountId(1L);
    private static final UserAccountId FARMER_ID = new UserAccountId(2L);

    @Test
    void requestCreatesAPendingLinkWithoutDomainEvents() {
        AdvisoryLink link = AdvisoryLink.request(ADVISOR_ID, FARMER_ID);

        assertThat(link.getStatus()).isEqualTo(LinkStatus.PENDING);
        assertThat(link.isActive()).isFalse();
        assertThat(link.pullDomainEvents()).isEmpty();
    }

    @Test
    void requestRejectsAnAdvisorLinkingToThemselves() {
        assertThatThrownBy(() -> AdvisoryLink.request(ADVISOR_ID, ADVISOR_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void acceptTransitionsAPendingLinkAndPublishesAdvisoryLinkAcceptedEvent() {
        AdvisoryLink link = AdvisoryLink.request(ADVISOR_ID, FARMER_ID);

        link.accept();

        assertThat(link.getStatus()).isEqualTo(LinkStatus.ACCEPTED);
        assertThat(link.isActive()).isTrue();
        assertThat(link.getRespondedAt()).isPresent();
        assertThat(link.pullDomainEvents()).hasSize(1).first().isInstanceOf(AdvisoryLinkAcceptedEvent.class);
    }

    @Test
    void acceptFailsWhenTheLinkIsNotPending() {
        AdvisoryLink link = AdvisoryLink.request(ADVISOR_ID, FARMER_ID);
        link.accept();

        assertThatThrownBy(link::accept).isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void acceptFailsWhenTheLinkWasAlreadyRevoked() {
        AdvisoryLink link = AdvisoryLink.request(ADVISOR_ID, FARMER_ID);
        link.revoke();

        assertThatThrownBy(link::accept).isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void revokeTransitionsTheLinkAndPublishesAdvisoryLinkRevokedEvent() {
        AdvisoryLink link = AdvisoryLink.request(ADVISOR_ID, FARMER_ID);
        link.accept();
        link.pullDomainEvents();

        link.revoke();

        assertThat(link.getStatus()).isEqualTo(LinkStatus.REVOKED);
        assertThat(link.isActive()).isFalse();
        assertThat(link.pullDomainEvents()).hasSize(1).first().isInstanceOf(AdvisoryLinkRevokedEvent.class);
    }

    @Test
    void revokeFailsWhenTheLinkIsAlreadyRevoked() {
        AdvisoryLink link = AdvisoryLink.request(ADVISOR_ID, FARMER_ID);
        link.revoke();

        assertThatThrownBy(link::revoke).isInstanceOf(BusinessRuleViolationException.class);
    }
}
