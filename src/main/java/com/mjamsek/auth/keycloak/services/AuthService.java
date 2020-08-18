package com.mjamsek.auth.keycloak.services;

import com.mjamsek.auth.keycloak.context.AuthContext;

import javax.interceptor.InvocationContext;

public interface AuthService {
    
    void processSecurity(InvocationContext context);
    
    AuthContext produceContext(String rawToken);
    
}
