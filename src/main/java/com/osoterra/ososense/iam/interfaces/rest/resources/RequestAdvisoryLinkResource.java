package com.osoterra.ososense.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;

/**
 * The farmer to supervise, by email (what an advisor knows) or by account id.
 */
public record RequestAdvisoryLinkResource(Long farmerId, @Email String farmerEmail) {
}
