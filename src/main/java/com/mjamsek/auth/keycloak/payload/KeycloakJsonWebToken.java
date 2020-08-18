package com.mjamsek.auth.keycloak.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import org.keycloak.representations.JsonWebToken;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakJsonWebToken extends JsonWebToken {
    
    @JsonProperty("preferred_username")
    private String preferredUsername;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("realm_access")
    private Roles realmAccess;
    
    @JsonProperty("resource_access")
    private Map<String, Roles> resourceAccess;
    
    @JsonProperty("scope")
    private String scopes;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getScopes() {
        return scopes;
    }
    
    public void setScopes(String scopes) {
        this.scopes = scopes;
    }
    
    public String getPreferredUsername() {
        return preferredUsername;
    }
    
    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }
    
    public Roles getRealmAccess() {
        return realmAccess;
    }
    
    public void setRealmAccess(Roles realmAccess) {
        this.realmAccess = realmAccess;
    }
    
    public Map<String, Roles> getResourceAccess() {
        return resourceAccess;
    }
    
    public void setResourceAccess(Map<String, Roles> resourceAccess) {
        this.resourceAccess = resourceAccess;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Roles {
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        @JsonProperty("roles")
        private List<String> roles;
        
        public List<String> getRoles() {
            return roles;
        }
        
        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
