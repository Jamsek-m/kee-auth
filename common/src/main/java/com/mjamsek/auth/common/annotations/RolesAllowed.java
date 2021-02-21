package com.mjamsek.auth.common.annotations;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allow only authenticated users with role to access annotated method
 * @author Miha Jamsek
 * @since 2.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesAllowed {
    @Nonbinding String[] value() default {};
    @Nonbinding String clientName() default "";
}
