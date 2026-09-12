package com.osoterra.ososense.iam.interfaces.rest.controllers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binds a controller parameter to the {@code UserAccountId} of the request's authenticated
 * principal. The identity is resolved once, at the perimeter, and never from the path or
 * body of the request.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserId {
}
