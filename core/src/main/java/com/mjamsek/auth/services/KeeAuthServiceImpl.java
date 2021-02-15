package com.mjamsek.auth.services;

import com.mjamsek.auth.common.annotations.AuthenticatedAllowed;
import com.mjamsek.auth.common.annotations.PublicResource;
import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.annotations.ScopesAllowed;
import com.mjamsek.auth.common.exceptions.UnresolvableRolesException;
import com.mjamsek.auth.common.models.AnnotationResult;
import com.mjamsek.auth.common.resolvers.RolesResolver;
import com.mjamsek.auth.common.utils.AnnotationUtil;
import com.mjamsek.auth.context.AuthContext;
import io.jsonwebtoken.Claims;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class KeeAuthServiceImpl implements KeeAuthService {
    
    private static final Logger LOG = Logger.getLogger(KeeAuthServiceImpl.class.getName());
    
    @Inject
    private AuthContext authContext;
    
    @Override
    public void processSecurity(InvocationContext context) {
        AnnotationResult<RolesAllowed> rolesAllowed = AnnotationUtil.getRolesAllowedAnnotation(context.getMethod());
        if (rolesAllowed.hasAnnotation()) {
            if (this.isNotPublic(rolesAllowed, context.getMethod())) {
                this.validateRoles(rolesAllowed.getAnnotation());
            }
        } else {
            AnnotationResult<ScopesAllowed> scopesAllowed = AnnotationUtil.getScopesAllowedAnnotation(context.getMethod());
            if (scopesAllowed.hasAnnotation()) {
                if (this.isNotPublic(scopesAllowed, context.getMethod())) {
                    this.validateScopes(scopesAllowed.getAnnotation());
                }
            } else {
                AnnotationResult<AuthenticatedAllowed> authenticatedAllowed = AnnotationUtil.getAuthenticatedAllowedAnnotation(context.getMethod());
                if (authenticatedAllowed.hasAnnotation()) {
                    if (this.isNotPublic(authenticatedAllowed, context.getMethod())) {
                        this.validateAuthenticated();
                    }
                }
            }
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
    
    /**
     * Validates that valid credentials are presented
     *
     * @throws NotAuthorizedException when no or invalid credentials are presented
     */
    private void validateAuthenticated() throws NotAuthorizedException {
        if (!authContext.isAuthenticated()) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
    
    /**
     * Validates that credentials have proper rights
     *
     * @param annotation annotation from invoked method
     * @throws NotAuthorizedException when no or invalid credentials are presented
     * @throws ForbiddenException     when valid credentials don't have proper rights
     */
    private void validateRoles(RolesAllowed annotation) throws NotAuthorizedException, ForbiddenException {
        this.validateAuthenticated();
        
        Set<String> allowedRoles = Set.of(annotation.value());
        Set<String> userRoles = this.getUserRoles(annotation);
        
        boolean hasRole = !Collections.disjoint(userRoles, allowedRoles);
        if (!hasRole) {
            throw new ForbiddenException(Response.status(Response.Status.FORBIDDEN).build());
        }
    }
    
    /**
     * Returns credentials roles from JWT claims
     *
     * @param annotation annotation from invoked method
     * @return set of granted roles
     */
    private Set<String> getUserRoles(RolesAllowed annotation) {
        Claims payload = authContext.getTokenPayload();
        Set<String> userRoles = null;
        
        List<RolesResolver> rolesResolvers = getRoleResolvers();
        for (RolesResolver rolesResolver : rolesResolvers) {
            try {
                userRoles = rolesResolver.resolveRoles(payload, annotation);
                LOG.fine("Using resolver " + rolesResolver.getName() + " for mapping claims.");
                break;
            } catch (UnresolvableRolesException e) {
                LOG.warning("Error when invoking resolver " + e.getResolverName() +
                    "! Error: " + e.getMessage() + " Proceeding to invoke next resolver.");
            } catch (Exception e) {
                LOG.severe(e.getMessage());
            }
        }
        
        if (userRoles == null) {
            return new HashSet<>();
        }
        return userRoles;
    }
    
    /**
     * Validates that credentials have proper scopes
     *
     * @param annotation annotation from invoked method
     * @throws NotAuthorizedException when no or invalid credentials are presented
     * @throws ForbiddenException     when valid credentials don't have proper scopes
     */
    private void validateScopes(ScopesAllowed annotation) throws NotAuthorizedException, ForbiddenException {
        this.validateAuthenticated();
        
        Set<String> allowedScopes = Set.of(annotation.value());
        Set<String> userRoles = authContext.getScope();
        
        boolean hasRole = !Collections.disjoint(userRoles, allowedScopes);
        if (!hasRole) {
            throw new ForbiddenException(Response.status(Response.Status.FORBIDDEN).build());
        }
    }
    
    /**
     * Returns enabled role resolver implementations in sorted order by priority
     *
     * @return list of resolvers
     */
    private List<RolesResolver> getRoleResolvers() {
        ServiceLoader<RolesResolver> loader = ServiceLoader.load(RolesResolver.class);
        
        return loader.stream()
            .filter(provider -> provider.get().isEnabled())
            .sorted(Comparator
                .comparingInt((ServiceLoader.Provider<RolesResolver> resolver) -> resolver.get().getPriority())
                .reversed())
            .map(ServiceLoader.Provider::get)
            .collect(Collectors.toUnmodifiableList());
    }
    
}
