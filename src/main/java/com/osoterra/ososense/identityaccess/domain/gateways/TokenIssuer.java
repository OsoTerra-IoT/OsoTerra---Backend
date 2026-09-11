package com.osoterra.ososense.identityaccess.domain.gateways;

import com.osoterra.ososense.identityaccess.domain.model.UserAccount;

/**
 * Port through which the domain asks for an access token for an authenticated account,
 * without knowing the concrete token technology.
 */
public interface TokenIssuer {

    IssuedToken issueFor(UserAccount account);
}
