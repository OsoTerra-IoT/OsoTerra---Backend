package com.osoterra.ososense.identityaccess.domain.model;

import com.osoterra.ososense.identityaccess.domain.events.UserRegisteredEvent;
import com.osoterra.ososense.identityaccess.domain.services.PasswordHashingService;
import com.osoterra.ososense.shared.AggregateRoot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Aggregate root for a platform user account. Guarantees email uniqueness (enforced
 * together with the repository), role validity, and credential integrity.
 */
public final class UserAccount extends AggregateRoot<UserAccountId> {

    private final EmailAddress email;
    private PasswordHash passwordHash;
    private final PersonName name;
    private final UserRole role;
    private final ProfessionalLicense license;
    private boolean isActive;
    private final LocalDateTime createdAt;

    private UserAccount(
            UserAccountId id,
            EmailAddress email,
            PasswordHash passwordHash,
            PersonName name,
            UserRole role,
            ProfessionalLicense license,
            boolean isActive,
            LocalDateTime createdAt) {
        super(id);
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.role = role;
        this.license = license;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    /**
     * Creates a new account and raises {@link UserRegisteredEvent}. The advisor license
     * must be present when the role is {@link UserRole#ADVISOR} and absent otherwise.
     */
    public static UserAccount register(
            EmailAddress email,
            PasswordHash passwordHash,
            PersonName name,
            UserRole role,
            ProfessionalLicense license) {
        if (role == UserRole.ADVISOR && license == null) {
            throw new IllegalArgumentException("An advisor account requires a professional license");
        }
        if (role == UserRole.FARMER && license != null) {
            throw new IllegalArgumentException("A farmer account must not have a professional license");
        }
        UserAccount account = new UserAccount(null, email, passwordHash, name, role, license, true, LocalDateTime.now());
        account.registerEvent(new UserRegisteredEvent(email, role, Instant.now()));
        return account;
    }

    /**
     * Rebuilds an account from persisted data without raising any domain event.
     */
    public static UserAccount reconstruct(
            UserAccountId id,
            EmailAddress email,
            PasswordHash passwordHash,
            PersonName name,
            UserRole role,
            ProfessionalLicense license,
            boolean isActive,
            LocalDateTime createdAt) {
        return new UserAccount(id, email, passwordHash, name, role, license, isActive, createdAt);
    }

    public boolean verifyPassword(String rawPassword, PasswordHashingService hashingService) {
        return hashingService.matches(rawPassword, passwordHash);
    }

    public void changePassword(PasswordHash newHash) {
        this.passwordHash = newHash;
    }

    public boolean isAdvisor() {
        return role == UserRole.ADVISOR;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public PersonName getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public Optional<ProfessionalLicense> getLicense() {
        return Optional.ofNullable(license);
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
