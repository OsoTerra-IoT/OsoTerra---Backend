package com.osoterra.ososense.iam.domain.model;

import com.osoterra.ososense.iam.domain.events.UserRegisteredEvent;
import com.osoterra.ososense.iam.domain.services.PasswordHashingService;
import com.osoterra.ososense.shared.domain.model.AggregateRoot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for a platform user account. Guarantees email uniqueness (enforced
 * together with the repository), role validity, and credential integrity.
 */
public final class UserAccount extends AggregateRoot<UserAccountId> {

    private final EmailAddress email;
    private PasswordHash passwordHash;
    private String googleAccountId;
    private final PersonName name;
    private final UserRole role;
    private final ProfessionalLicense license;
    private boolean isActive;
    private final LocalDateTime createdAt;

    private UserAccount(
            UserAccountId id,
            EmailAddress email,
            PasswordHash passwordHash,
            String googleAccountId,
            PersonName name,
            UserRole role,
            ProfessionalLicense license,
            boolean isActive,
            LocalDateTime createdAt) {
        super(id);
        if (passwordHash == null && (googleAccountId == null || googleAccountId.isBlank())) {
            throw new IllegalArgumentException("An account requires either a password or a linked Google account");
        }
        this.email = email;
        this.passwordHash = passwordHash;
        this.googleAccountId = googleAccountId;
        this.name = name;
        this.role = role;
        this.license = license;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    /**
     * Creates a new account authenticated by password and raises {@link UserRegisteredEvent}.
     * The advisor license must be present when the role is {@link UserRole#ADVISOR} and
     * absent otherwise.
     */
    public static UserAccount register(
            EmailAddress email,
            PasswordHash passwordHash,
            PersonName name,
            UserRole role,
            ProfessionalLicense license) {
        validateLicense(role, license);
        UserAccount account = new UserAccount(
                null, email, passwordHash, null, name, role, license, true, LocalDateTime.now());
        account.registerEvent(new UserRegisteredEvent(email, role, Instant.now()));
        return account;
    }

    /**
     * Creates a new account authenticated by a verified Google identity, with no local
     * password. Raises {@link UserRegisteredEvent} exactly like {@link #register}.
     */
    public static UserAccount registerViaGoogle(
            EmailAddress email,
            PersonName name,
            UserRole role,
            ProfessionalLicense license,
            String googleAccountId) {
        Objects.requireNonNull(googleAccountId, "googleAccountId");
        validateLicense(role, license);
        UserAccount account = new UserAccount(
                null, email, null, googleAccountId, name, role, license, true, LocalDateTime.now());
        account.registerEvent(new UserRegisteredEvent(email, role, Instant.now()));
        return account;
    }

    private static void validateLicense(UserRole role, ProfessionalLicense license) {
        if (role == UserRole.ADVISOR && license == null) {
            throw new IllegalArgumentException("An advisor account requires a professional license");
        }
        if (role == UserRole.FARMER && license != null) {
            throw new IllegalArgumentException("A farmer account must not have a professional license");
        }
    }

    /**
     * Rebuilds an account from persisted data without raising any domain event.
     */
    public static UserAccount reconstruct(
            UserAccountId id,
            EmailAddress email,
            PasswordHash passwordHash,
            String googleAccountId,
            PersonName name,
            UserRole role,
            ProfessionalLicense license,
            boolean isActive,
            LocalDateTime createdAt) {
        return new UserAccount(id, email, passwordHash, googleAccountId, name, role, license, isActive, createdAt);
    }

    public boolean verifyPassword(String rawPassword, PasswordHashingService hashingService) {
        return passwordHash != null && hashingService.matches(rawPassword, passwordHash);
    }

    public void changePassword(PasswordHash newHash) {
        this.passwordHash = newHash;
    }

    /**
     * Links a verified Google identity to an existing password-based account, so the same
     * user can subsequently sign in either way.
     */
    public void linkGoogleAccount(String googleAccountId) {
        Objects.requireNonNull(googleAccountId, "googleAccountId");
        this.googleAccountId = googleAccountId;
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

    public Optional<PasswordHash> getPasswordHash() {
        return Optional.ofNullable(passwordHash);
    }

    public Optional<String> getGoogleAccountId() {
        return Optional.ofNullable(googleAccountId);
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
