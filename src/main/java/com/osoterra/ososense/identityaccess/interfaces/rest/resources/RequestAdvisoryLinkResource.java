package com.osoterra.ososense.identityaccess.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record RequestAdvisoryLinkResource(@NotNull Long farmerId) {
}
