package com.mjamsek.auth.keycloak.context;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

public class AuthContext {
    
    /**
     * User id, retrieved from JWT subject
     */
    String id;
    
    /**
     * Username, retrieved from JWT. By default field <code>preferred_username</code> is used.
     */
    String username;
    
    /**
     * User email, retrieved from JWT. By default field <code>email</code> is used.
     */
    String email;
    
    /**
     * User realm roles
     */
    List<String> realmRoles;
    
    /**
     * User client roles
     */
    MultivaluedMap<String, String> clientRoles;
    
    /**
     * User scopes
     */
    List<String> scopes;
    
    /**
     * Is true, when auth context is constructed
     */
    boolean authenticated;
    
    /**
     * Map of all other claims
     */
    Map<String, Object> claims;
    
    /**
     * Raw JWT token
     */
    String rawToken;
    
    /**
     * Checks if user has realm role
     * @param role role id
     * @return true if user has role in this realm, false otherwise
     */
    public boolean hasRealmRole(String role) {
        return realmRoles.contains(role);
    }
    
    /**
     * Checks if user has client role on given client
     * @param clientId client id
     * @param role role id
     * @return true if user has role in this client, false otherwise
     */
    public boolean hasClientRole(String clientId, String role) {
        if (clientRoles.containsKey(clientId)) {
            return clientRoles.get(clientId).contains(role);
        }
        return false;
    }
    
    /**
     * Checks if user has scope
     * @param scope scope id
     * @return true if user has scope, false otherwise
     */
    public boolean hasScope(String scope) {
        return scopes.contains(scope);
    }
    
    /**
     * Checks if user has realm role or client role on any client
     * @param role role id
     * @return true if user has role on realm or client, false otherwise
     */
    public boolean hasRole(String role) {
        if (hasRealmRole(role)) {
            return true;
        }
        for (String clientId : clientRoles.keySet()) {
            if (hasClientRole(clientId, role)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if user has claim present
     * @param claim claim name
     * @return true if user has claim, false otherwise
     */
    public boolean hasClaim(String claim) {
        return claims.containsKey(claim);
    }
    
    /**
     * Get user's roles on given client
     * @param clientId client id
     * @return list of granted roles on client
     */
    public List<String> getClientRoles(String clientId) {
        if (clientRoles.containsKey(clientId)) {
            return clientRoles.get(clientId);
        }
        return null;
    }
    
    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public List<String> getRealmRoles() {
        return realmRoles;
    }
    
    public MultivaluedMap<String, String> getClientRoles() {
        return clientRoles;
    }
    
    public List<String> getScopes() {
        return scopes;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    public Map<String, Object> getClaims() {
        return claims;
    }
    
    public String getRawToken() {
        return rawToken;
    }
}
