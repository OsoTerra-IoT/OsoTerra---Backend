package com.osoterra.ososense.iam.domain.services;

import com.osoterra.ososense.iam.domain.model.AdvisoryLink;

public interface RevokeAdvisoryLinkCommandService {

    AdvisoryLink handle(RevokeAdvisoryLinkCommand command);
}
