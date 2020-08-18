package com.mjamsek.auth.keycloak.utils;

import com.mjamsek.auth.keycloak.annotations.*;

import java.lang.reflect.Method;

public class AnnotationUtil {
    
    public static AnnotationResult<RolesAllowed> getRolesAllowedAnnotation(Method method) {
        RolesAllowed rolesAllowedAnnotation = method.getDeclaredAnnotation(RolesAllowed.class);
        if (rolesAllowedAnnotation == null) {
            return new AnnotationResult<>(method.getDeclaringClass().getDeclaredAnnotation(RolesAllowed.class), true);
        } else {
            return new AnnotationResult<>(rolesAllowedAnnotation, false);
        }
    }
    
    public static AnnotationResult<RealmRolesAllowed> getRealmRolesAllowedAnnotation(Method method) {
        RealmRolesAllowed realmRolesAllowed = method.getDeclaredAnnotation(RealmRolesAllowed.class);
        if (realmRolesAllowed == null) {
            return new AnnotationResult<>(method.getDeclaringClass().getDeclaredAnnotation(RealmRolesAllowed.class), true);
        } else {
            return new AnnotationResult<>(realmRolesAllowed, false);
        }
    }
    
    public static AnnotationResult<ClientRolesAllowed> getClientRolesAllowedAnnotation(Method method) {
        ClientRolesAllowed clientRolesAllowed = method.getDeclaredAnnotation(ClientRolesAllowed.class);
        if (clientRolesAllowed == null) {
            return new AnnotationResult<>(method.getDeclaringClass().getDeclaredAnnotation(ClientRolesAllowed.class), true);
        } else {
            return new AnnotationResult<>(clientRolesAllowed, false);
        }
    }
    
    public static AnnotationResult<AuthenticatedAllowed> getAuthenticatedAllowedAnnotation(Method method) {
        AuthenticatedAllowed authenticatedAllowed = method.getDeclaredAnnotation(AuthenticatedAllowed.class);
        if (authenticatedAllowed == null) {
            return new AnnotationResult<>(method.getDeclaringClass().getDeclaredAnnotation(AuthenticatedAllowed.class), true);
        } else {
            return new AnnotationResult<>(authenticatedAllowed, false);
        }
    }
    
}
