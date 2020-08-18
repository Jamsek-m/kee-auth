package com.mjamsek.auth.keycloak.annotations;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allow only authenticated users with role on given client to access annotated method
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientRolesAllowed {
    @Nonbinding String client() default "";
    @Nonbinding String[] roles() default {};
}
