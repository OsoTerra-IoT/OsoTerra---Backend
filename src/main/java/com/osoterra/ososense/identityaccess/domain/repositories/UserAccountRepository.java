package com.osoterra.ososense.identityaccess.domain.repositories;

import com.osoterra.ososense.identityaccess.domain.model.EmailAddress;
import com.osoterra.ososense.identityaccess.domain.model.UserAccount;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;

import java.util.Optional;

public interface UserAccountRepository {

    UserAccount save(UserAccount account);

    Optional<UserAccount> findById(UserAccountId id);

    Optional<UserAccount> findByEmail(EmailAddress email);

    boolean existsByEmail(EmailAddress email);
}
