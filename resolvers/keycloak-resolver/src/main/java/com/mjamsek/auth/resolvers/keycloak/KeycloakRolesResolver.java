package com.mjamsek.auth.resolvers.keycloak;

import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.exceptions.UnresolvableRolesException;
import com.mjamsek.auth.common.mappings.ClientNameMapper;
import com.mjamsek.auth.common.resolvers.ResolverDef;
import com.mjamsek.auth.common.resolvers.RolesResolver;
import io.jsonwebtoken.Claims;

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
    public Set<String> resolveRoles(Claims claims, RolesAllowed annotation) throws UnresolvableRolesException {
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
