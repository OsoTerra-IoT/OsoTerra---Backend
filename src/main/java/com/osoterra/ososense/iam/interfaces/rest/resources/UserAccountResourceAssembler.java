package com.osoterra.ososense.iam.interfaces.rest.resources;

import com.osoterra.ososense.iam.domain.model.ProfessionalLicense;
import com.osoterra.ososense.iam.domain.model.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class UserAccountResourceAssembler {

    public UserAccountResource toResource(UserAccount account) {
        return new UserAccountResource(
                account.getId().value(),
                account.getEmail().value(),
                account.getName().firstName(),
                account.getName().lastName(),
                account.getRole().name(),
                account.getLicense().map(ProfessionalLicense::number).orElse(null),
                account.isActive(),
                account.getCreatedAt());
    }
}
