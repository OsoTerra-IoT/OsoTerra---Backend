package com.osoterra.ososense.identityaccess.domain.services;

import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLink;

public interface AcceptAdvisoryLinkCommandService {

    AdvisoryLink handle(AcceptAdvisoryLinkCommand command);
}
