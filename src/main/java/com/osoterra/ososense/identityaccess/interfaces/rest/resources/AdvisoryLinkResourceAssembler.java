package com.osoterra.ososense.identityaccess.interfaces.rest.resources;

import com.osoterra.ososense.identityaccess.domain.model.AdvisoryLink;
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
