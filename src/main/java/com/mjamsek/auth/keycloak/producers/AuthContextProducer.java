package com.mjamsek.auth.keycloak.producers;

import com.mjamsek.auth.keycloak.annotations.Token;
import com.mjamsek.auth.keycloak.context.AuthContext;
import com.mjamsek.auth.keycloak.services.AuthService;
import com.mjamsek.auth.keycloak.utils.TokenUtil;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

@RequestScoped
public class AuthContextProducer {
    
    @Context
    private HttpServletRequest request;
    
    @Inject
    private AuthService authService;
    
    @Produces
    @RequestScoped
    public AuthContext produceAuthContext() {
        String rawToken = request.getHeader("Authorization");
        return authService.produceContext(rawToken);
    }
    
    @Produces
    @Token
    public String produceRawToken() {
        String authorizationHeader = request.getHeader("Authorization");
        return TokenUtil.trimAuthHeader(authorizationHeader);
    }
}
