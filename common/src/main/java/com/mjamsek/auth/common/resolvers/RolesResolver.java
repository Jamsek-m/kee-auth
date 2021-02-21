/*
 *  Copyright (c) 2019-2021 Miha Jamsek and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
