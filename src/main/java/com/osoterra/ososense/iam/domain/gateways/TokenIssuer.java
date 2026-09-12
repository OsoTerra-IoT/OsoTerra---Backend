package com.osoterra.ososense.iam.domain.gateways;

import com.osoterra.ososense.iam.domain.model.UserAccount;

/**
 * Port through which the domain asks for an access token for an authenticated account,
 * without knowing the concrete token technology.
 */
public interface TokenIssuer {

    IssuedToken issueFor(UserAccount account);
}
