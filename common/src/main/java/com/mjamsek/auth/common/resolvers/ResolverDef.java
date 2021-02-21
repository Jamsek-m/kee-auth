package com.mjamsek.auth.common.resolvers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definition for implemented {@link RolesResolver}
 * @author Miha Jamsek
 * @since 2.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResolverDef {
    String name();
}
