package com.mjamsek.auth.context;

import io.jsonwebtoken.Claims;

import java.util.HashSet;
import java.util.Set;

public class AuthContext {
    
    String id;
    
    String username;
    
    String email;
    
    boolean authenticated;
    
    String rawToken;
    
    Claims tokenPayload;
    
    Set<String> scope;
    
    AuthContext() {
    
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
    
    public String getRawToken() {
        return rawToken;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    public Claims getTokenPayload() {
        return tokenPayload;
    }
    
    public Set<String> getScope() {
        return scope;
    }
    
    public static class Builder {
        
        private final AuthContext instance;
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static AuthContext newEmptyContext() {
            AuthContext context = new AuthContext();
            context.authenticated = false;
            return context;
        }
        
        private Builder() {
            this.instance = new AuthContext();
        }
        
        public Builder token(String rawToken) {
            this.instance.rawToken = rawToken;
            return this;
        }
        
        public Builder payload(Claims payload) {
            this.instance.tokenPayload = payload;
            return this;
        }
        
        public Builder id(String id) {
            this.instance.id = id;
            return this;
        }
        
        public Builder username(String username) {
            this.instance.username = username;
            return this;
        }
        
        public Builder email(String email) {
            this.instance.email = email;
            return this;
        }
        
        public Builder authenticated(boolean authenticated) {
            this.instance.authenticated = authenticated;
            return this;
        }
        
        public Builder scope(String scopes) {
            return this.scope(scopes, " ");
        }
        
        public Builder scope(String scopes, String delimitor) {
            if (scopes == null || scopes.isBlank()) {
                this.instance.scope = new HashSet<>();
            } else {
                this.instance.scope = Set.of(scopes.split(delimitor));
            }
            return this;
        }
        
        public AuthContext build() {
            return this.instance;
        }
        
    }
    
}
