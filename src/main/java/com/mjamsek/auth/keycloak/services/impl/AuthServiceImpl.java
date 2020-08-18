package com.mjamsek.auth.keycloak.services.impl;

import com.mjamsek.auth.keycloak.annotations.*;
import com.mjamsek.auth.keycloak.config.KeycloakConfig;
import com.mjamsek.auth.keycloak.context.AuthContext;
import com.mjamsek.auth.keycloak.context.AuthContextBuilder;
import com.mjamsek.auth.keycloak.payload.KeycloakJsonWebToken;
import com.mjamsek.auth.keycloak.services.AuthService;
import com.mjamsek.auth.keycloak.utils.AnnotationResult;
import com.mjamsek.auth.keycloak.utils.AnnotationUtil;
import com.mjamsek.auth.keycloak.utils.TokenUtil;
import org.keycloak.common.VerificationException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequestScoped
public class AuthServiceImpl implements AuthService {
    
    @Context
    private HttpServletRequest request;
    
    @Inject
    private AuthContext authContext;
    
    @Override
    public void processSecurity(InvocationContext context) {
        
        AnnotationResult<RolesAllowed> rolesAllowed = AnnotationUtil.getRolesAllowedAnnotation(context.getMethod());
        if (rolesAllowed.hasAnnotation()) {
            if (this.isNotPublic(rolesAllowed, context.getMethod())) {
                this.validateRoles(rolesAllowed.getAnnotation().value());
            }
        } else {
            AnnotationResult<RealmRolesAllowed> realmRolesAllowed = AnnotationUtil
                .getRealmRolesAllowedAnnotation(context.getMethod());
            if (realmRolesAllowed.hasAnnotation()) {
                if (this.isNotPublic(realmRolesAllowed, context.getMethod())) {
                    this.validateRealmRoles(realmRolesAllowed.getAnnotation().value());
                }
            } else {
                AnnotationResult<ClientRolesAllowed> clientRolesAllowed = AnnotationUtil
                    .getClientRolesAllowedAnnotation(context.getMethod());
                if (clientRolesAllowed.hasAnnotation()) {
                    ClientRolesAllowed clientRolesAllowedAnnotation = clientRolesAllowed.getAnnotation();
                    if (this.isNotPublic(clientRolesAllowed, context.getMethod())) {
                        this.validateClientRoles(
                            clientRolesAllowedAnnotation.client(),
                            clientRolesAllowedAnnotation.roles()
                        );
                    }
                } else {
                    AnnotationResult<AuthenticatedAllowed> authenticatedAllowed = AnnotationUtil
                        .getAuthenticatedAllowedAnnotation(context.getMethod());
                    if (authenticatedAllowed.hasAnnotation()) {
                        if (this.isNotPublic(authenticatedAllowed, context.getMethod())) {
                            this.validateAuthenticated();
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public AuthContext produceContext(String authorizationHeader) {
        String rawToken = TokenUtil.trimAuthHeader(authorizationHeader);
        if (rawToken == null) {
            return AuthContextBuilder.empty();
        }
        
        try {
            KeycloakJsonWebToken token = KeycloakConfig.getInstance().getVerifier().verifyToken(rawToken, KeycloakJsonWebToken.class);
    
            AuthContextBuilder authContextBuilder = AuthContextBuilder
                .newBuilder()
                .authenticated(true)
                .id(token.getSubject())
                .username(token.getPreferredUsername())
                .email(token.getEmail())
                .claims(token.getOtherClaims())
                .token(rawToken);
            
            String scopes = token.getScopes();
            authContextBuilder.scopes(Arrays.asList(scopes.split(" ")));
            
            List<String> realmRoles = token.getRealmAccess().getRoles();
            authContextBuilder.realmRoles(realmRoles);
    
            MultivaluedMap<String, String> clientRoles = new MultivaluedHashMap<>();
            Map<String, KeycloakJsonWebToken.Roles> clientRolesMap = token.getResourceAccess();
            clientRolesMap.keySet().forEach(clientId -> clientRoles.addAll(clientId, clientRolesMap.get(clientId).getRoles()));
            authContextBuilder.clientRoles(clientRoles);
            
            return authContextBuilder.build();
        } catch (VerificationException e) {
            return AuthContextBuilder.empty();
        }
    }
    
    /**
     * Returns true if method is not explicitly public resource.
     *
     * @param annotation annotation to be checked
     * @param method     executing method
     * @param <T>        auth annotation type
     * @return true if method is not public
     */
    private <T> boolean isNotPublic(AnnotationResult<T> annotation, Method method) {
        if (annotation.isClassAnnotated()) {
            PublicResource publicResource = method.getDeclaredAnnotation(PublicResource.class);
            return publicResource == null;
        }
        return true;
    }
    
    private void validateAuthenticated() throws NotAuthorizedException {
        if (!authContext.isAuthenticated()) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
    
    private void validateRealmRoles(String[] requiredRoles) throws NotAuthorizedException, ForbiddenException {
        this.validateAuthenticated();
        
        boolean hasRole = Set.of(requiredRoles).stream().anyMatch(role -> authContext.hasRealmRole(role));
        
        if (!hasRole) {
            throw new ForbiddenException(Response.status(Response.Status.FORBIDDEN).build());
        }
    }
    
    private void validateClientRoles(String clientId, String[] clientRoles) throws NotAuthorizedException, ForbiddenException {
        this.validateAuthenticated();
        
        boolean hasRole = Set.of(clientRoles).stream().anyMatch(role -> authContext.hasClientRole(clientId, role));
        
        if (!hasRole) {
            throw new ForbiddenException(Response.status(Response.Status.FORBIDDEN).build());
        }
    }
    
    private void validateRoles(String[] roles) throws NotAuthorizedException, ForbiddenException {
        this.validateAuthenticated();
        boolean realmRoleFound = false;
        boolean clientRoleFound = false;
        try {
            this.validateRealmRoles(roles);
            realmRoleFound = true;
        } catch (ForbiddenException ignored) {
        }
        for (String clientId : authContext.getClientRoles().keySet()) {
            try {
                this.validateClientRoles(clientId, roles);
                clientRoleFound = true;
            } catch (ForbiddenException ignored) {
            }
        }
        
        if (!realmRoleFound && !clientRoleFound) {
            throw new ForbiddenException(Response.status(Response.Status.FORBIDDEN).build());
        }
    }
}
