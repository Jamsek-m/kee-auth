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
package com.mjamsek.auth.resolvers;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.common.exceptions.UnresolvableRolesException;
import com.mjamsek.auth.common.resolvers.ResolverDef;
import com.mjamsek.auth.common.resolvers.RolesResolver;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default resolver for roles. Expects role claim to be string array (default role claim is 'roles')
 * @author Miha Jamsek
 * @since 2.0.0
 */
@ResolverDef(name = "default")
public class DefaultRolesResolver implements RolesResolver {
    
    public static final int PRIORITY = 0;
    
    @Override
    public int getPriority() {
        return PRIORITY;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Set<String> resolveRoles(Map<String, Object> claims, RolesAllowed annotation) throws UnresolvableRolesException {
        Object rolesObject = claims.get(getRoleClaimName());
        if (rolesObject instanceof List) {
            List<Object> roleItemsList = (List<Object>) rolesObject;
            return roleItemsList.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
        } else {
            throw new UnresolvableRolesException(this.getName(), "Expected array of strings.");
        }
    }
    
    private String getRoleClaimName() {
        return ConfigurationUtil.getInstance().get(ConfigKeys.RoleResolvers.DEFAULT_ROLES_MAPPING).orElse("roles");
    }
    
}
