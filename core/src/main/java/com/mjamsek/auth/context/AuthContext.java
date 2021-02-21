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
package com.mjamsek.auth.context;

import io.jsonwebtoken.Claims;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
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
