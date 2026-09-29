package com.osoterra.ososense.shared.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binds a controller parameter to the numeric id of the request's authenticated principal.
 * The identity is resolved once, at the perimeter, and never from the path or body of the
 * request. Each bounded context wraps the raw id into its own typed identifier as needed.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserId {
}
