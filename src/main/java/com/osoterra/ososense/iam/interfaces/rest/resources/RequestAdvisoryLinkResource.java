package com.osoterra.ososense.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record RequestAdvisoryLinkResource(@NotNull Long farmerId) {
}
