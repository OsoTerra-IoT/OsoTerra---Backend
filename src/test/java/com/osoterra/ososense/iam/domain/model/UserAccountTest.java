package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.iam.domain.events.UserRegisteredEvent;
import com.osoterra.ososense.iam.domain.services.PasswordHashingService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserAccountTest {

    private static final PasswordHashingService STUB_HASHING = new PasswordHashingService() {
        @Override
        public PasswordHash hash(String rawPassword) {
            return new PasswordHash(rawPassword + "-hashed", "STUB");
        }

        @Override
        public boolean matches(String rawPassword, PasswordHash hash) {
            return hash.value().equals(rawPassword + "-hashed");
        }
    };

    @Test
    void registerCreatesAnActiveFarmerAccountAndPublishesUserRegisteredEvent() {
        UserAccount account = UserAccount.register(
                new EmailAddress("farmer@osoterra.com"),
                STUB_HASHING.hash("s3cret!"),
                new PersonName("Ana", "Quispe"),
                UserRole.FARMER,
                null);

        assertThat(account.isActive()).isTrue();
        assertThat(account.isAdvisor()).isFalse();

        var events = account.pullDomainEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOfSatisfying(UserRegisteredEvent.class, event -> {
            assertThat(event.email().value()).isEqualTo("farmer@osoterra.com");
            assertThat(event.role()).isEqualTo(UserRole.FARMER);
        });
    }

    @Test
    void pullDomainEventsClearsThePendingEvents() {
        UserAccount account = UserAccount.register(
                new EmailAddress("farmer2@osoterra.com"),
                STUB_HASHING.hash("s3cret!"),
                new PersonName("Ana", "Quispe"),
                UserRole.FARMER,
                null);

        account.pullDomainEvents();

        assertThat(account.pullDomainEvents()).isEmpty();
    }

    @Test
    void registerRejectsAnAdvisorWithoutAProfessionalLicense() {
        assertThatThrownBy(() -> UserAccount.register(
                new EmailAddress("advisor@osoterra.com"),
                STUB_HASHING.hash("s3cret!"),
                new PersonName("Luis", "Ramos"),
                UserRole.ADVISOR,
                null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void registerRejectsAFarmerWithAProfessionalLicense() {
        assertThatThrownBy(() -> UserAccount.register(
                new EmailAddress("farmer3@osoterra.com"),
                STUB_HASHING.hash("s3cret!"),
                new PersonName("Ana", "Quispe"),
                UserRole.FARMER,
                new ProfessionalLicense("CIP-12345")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void verifyPasswordDelegatesToTheHashingService() {
        UserAccount account = UserAccount.register(
                new EmailAddress("farmer4@osoterra.com"),
                STUB_HASHING.hash("s3cret!"),
                new PersonName("Ana", "Quispe"),
                UserRole.FARMER,
                null);

        assertThat(account.verifyPassword("s3cret!", STUB_HASHING)).isTrue();
        assertThat(account.verifyPassword("wrong", STUB_HASHING)).isFalse();
    }

    @Test
    void changePasswordReplacesTheStoredHash() {
        UserAccount account = UserAccount.register(
                new EmailAddress("farmer5@osoterra.com"),
                STUB_HASHING.hash("old-pass"),
                new PersonName("Ana", "Quispe"),
                UserRole.FARMER,
                null);

        account.changePassword(STUB_HASHING.hash("new-pass"));

        assertThat(account.verifyPassword("new-pass", STUB_HASHING)).isTrue();
        assertThat(account.verifyPassword("old-pass", STUB_HASHING)).isFalse();
    }

    @Test
    void deactivateMakesTheAccountInactive() {
        UserAccount account = UserAccount.register(
                new EmailAddress("farmer6@osoterra.com"),
                STUB_HASHING.hash("s3cret!"),
                new PersonName("Ana", "Quispe"),
                UserRole.FARMER,
                null);

        account.deactivate();

        assertThat(account.isActive()).isFalse();
    }
}
