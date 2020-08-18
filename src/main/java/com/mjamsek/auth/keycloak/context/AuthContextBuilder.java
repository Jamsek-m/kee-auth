package com.mjamsek.auth.keycloak.context;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

public class AuthContextBuilder {
    
    private final AuthContext instance;
    
    public static AuthContextBuilder newBuilder() {
        return new AuthContextBuilder();
    }
    
    public static AuthContext empty() {
        AuthContext context = new AuthContext();
        context.authenticated = false;
        return context;
    }
    
    private AuthContextBuilder() {
        this.instance = new AuthContext();
    }
    
    public AuthContextBuilder token(String rawToken) {
        this.instance.rawToken = rawToken;
        return this;
    }
    
    public AuthContextBuilder id(String id) {
        this.instance.id = id;
        return this;
    }
    
    public AuthContextBuilder username(String username) {
        this.instance.username = username;
        return this;
    }
    
    public AuthContextBuilder email(String email) {
        this.instance.email = email;
        return this;
    }
    
    public AuthContextBuilder realmRoles(List<String> realmRoles) {
        this.instance.realmRoles = realmRoles;
        return this;
    }
    
    public AuthContextBuilder clientRoles(MultivaluedMap<String, String> clientRoles) {
        this.instance.clientRoles = clientRoles;
        return this;
    }
    
    public AuthContextBuilder scopes(List<String> scopes) {
        this.instance.scopes = scopes;
        return this;
    }
    
    public AuthContextBuilder authenticated(boolean authenticated) {
        this.instance.authenticated = authenticated;
        return this;
    }
    
    public AuthContextBuilder claims(Map<String, Object> claims) {
        this.instance.claims = claims;
        return this;
    }
    
    public AuthContext build() {
        return this.instance;
    }
    
}
