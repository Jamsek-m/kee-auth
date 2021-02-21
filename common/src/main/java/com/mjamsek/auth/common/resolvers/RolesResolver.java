package com.mjamsek.auth.common.resolvers;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.exceptions.UnresolvableRolesException;
import io.jsonwebtoken.Claims;

import javax.annotation.Priority;
import java.util.Set;

import static com.mjamsek.auth.common.config.ConfigKeys.RESOLVER_ENABLED_POSTFIX;
import static com.mjamsek.auth.common.config.ConfigKeys.RESOLVER_ENABLED_PREFIX;

/**
 * Interface for resolving roles
 * @author Miha Jamsek
 * @since 2.0.0
 */
public interface RolesResolver {
    
    int DEFAULT_PRIORITY = 100;
    
    /**
     * Determines priority of resolver if more than one are present, where higher the value, higher the priority.
     * Default method first checks for presence of {@link Priority} annotation and otherwise defaults to {@link RolesResolver#DEFAULT_PRIORITY}.
     * @see RolesResolver
     * @return priority
     */
    default int getPriority() {
        Priority priority = this.getClass().getAnnotation(Priority.class);
        if (priority != null) {
            return priority.value();
        }
        return DEFAULT_PRIORITY;
    }
    
    /**
     * Determines whether resolver should be used at all.
     * @return true if enabled, false otherwise
     */
    default boolean isEnabled() {
        ResolverDef resolverDef = this.getClass().getAnnotation(ResolverDef.class);
        if (resolverDef != null) {
            String configKey = RESOLVER_ENABLED_PREFIX + "." +
                resolverDef.name() + "." + RESOLVER_ENABLED_POSTFIX;
            return ConfigurationUtil.getInstance().getBoolean(configKey).orElse(true);
        }
        return false;
    }
    
    /**
     * Return resolver name
     * @return resolver name
     */
    default String getName() {
        ResolverDef resolverDef = RolesResolver.class.getAnnotation(ResolverDef.class);
        if (resolverDef != null) {
            return resolverDef.name();
        }
        return null;
    }
    
    /**
     * Custom resolver for retrieving roles from JWT claims
     * @param claims JWT payload claims
     * @param annotation annotation on method, from which optional client id can be retrieved
     * @throws UnresolvableRolesException  if resolver was unable to resolve roles from given claims
     * @return list of user roles
     */
    Set<String> resolveRoles(Claims claims, RolesAllowed annotation) throws UnresolvableRolesException;
    
}
