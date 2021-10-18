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
package com.mjamsek.auth.resolvers.keycloak;

import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.exceptions.UnresolvableRolesException;
import com.mjamsek.auth.common.mappings.ClientNameMapper;
import com.mjamsek.auth.common.resolvers.ResolverDef;
import com.mjamsek.auth.common.resolvers.RolesResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
@ResolverDef(name = "keycloak")
public class KeycloakRolesResolver implements RolesResolver {
    
    public static final int PRIORITY = 200;
    
    private static final String REALM_ACCESS_KEY = "realm_access";
    private static final String CLIENT_ACCESS_KEY = "resource_access";
    private static final String ROLES_KEY = "roles";
    
    @Override
    public int getPriority() {
        return PRIORITY;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Set<String> resolveRoles(Map<String, Object> claims, RolesAllowed annotation) throws UnresolvableRolesException {
        String clientName = annotation.clientName();
        if (clientName.isEmpty() || clientName.isBlank()) {
            // Retrieve access node for realm access
            
            Map<String, ?> realmAccessMap = (Map<String, ?>) claims.get(REALM_ACCESS_KEY);
            if (realmAccessMap == null) {
                throw new UnresolvableRolesException(this.getName(), "Resolver needs claim '" + REALM_ACCESS_KEY + "' to be present!");
            }
            return parseRoles(realmAccessMap);
        }
        // Resolve client name to client id
        String clientId = ClientNameMapper.getClientRoleMappings().getOrDefault(clientName, clientName);
        // Retrieve access node for specified client id
        Map<String, Map<String, ?>> clientNodes = (Map<String, Map<String, ?>>) claims.get(CLIENT_ACCESS_KEY);
        if (clientNodes == null) {
            throw new UnresolvableRolesException(this.getName(), "Resolver needs claim '" + CLIENT_ACCESS_KEY + "' to be present!");
        }
        Map<String, ?> clientAccessMap = null;
        if (clientNodes.containsKey(clientId)) {
            clientAccessMap = clientNodes.get(clientId);
        } else if (clientNodes.containsKey(clientName)) {
            clientAccessMap = clientNodes.get(clientName);
        }
        if (clientAccessMap != null) {
            return parseRoles(clientAccessMap);
        }
        return new HashSet<>();
    }
    
    @SuppressWarnings("unchecked")
    private Set<String> parseRoles(Map<String, ?> rolesMap) throws UnresolvableRolesException {
        Object rolesList = rolesMap.get(ROLES_KEY);
        if (rolesList instanceof List) {
            List<Object> roleItemsList = (List<Object>) rolesList;
            return roleItemsList.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
        } else {
            throw new UnresolvableRolesException(this.getName(), "Expected array of strings!");
        }
    }
    
}
