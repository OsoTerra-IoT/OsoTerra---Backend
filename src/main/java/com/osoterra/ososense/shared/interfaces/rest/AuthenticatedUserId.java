package com.osoterra.ososense.shared.interfaces.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injects the id of the authenticated user into a {@code Long} controller parameter.
 * Lets every bounded context know who is calling without depending on the identity and
 * access management model.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedUserId {
}
