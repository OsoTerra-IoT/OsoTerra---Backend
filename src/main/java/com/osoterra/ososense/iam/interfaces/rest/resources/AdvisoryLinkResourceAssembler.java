package com.osoterra.ososense.iam.interfaces.rest.resources;

import com.osoterra.ososense.iam.domain.model.AdvisoryLink;
import org.springframework.stereotype.Component;

@Component
public class AdvisoryLinkResourceAssembler {

    public AdvisoryLinkResource toResource(AdvisoryLink link) {
        return new AdvisoryLinkResource(
                link.getId().value(),
                link.getAdvisorId().value(),
                link.getFarmerId().value(),
                link.getStatus().name(),
                link.getRequestedAt(),
                link.getRespondedAt().orElse(null));
    }
}
