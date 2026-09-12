package com.osoterra.ososense.iam.domain.services;

import com.osoterra.ososense.iam.domain.model.AdvisoryLink;

public interface AcceptAdvisoryLinkCommandService {

    AdvisoryLink handle(AcceptAdvisoryLinkCommand command);
}
