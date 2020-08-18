package com.mjamsek.auth.keycloak.annotations;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allow only authenticated users with realm role to access annotated method
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RealmRolesAllowed {
    @Nonbinding String[] value() default {};
}
