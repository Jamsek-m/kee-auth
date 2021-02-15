package com.mjamsek.auth.common.utils;


import com.mjamsek.auth.common.annotations.AuthenticatedAllowed;
import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.annotations.ScopesAllowed;
import com.mjamsek.auth.common.models.AnnotationResult;

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
    
    public static AnnotationResult<ScopesAllowed> getScopesAllowedAnnotation(Method method) {
        ScopesAllowed scopesAllowedAnnotation = method.getDeclaredAnnotation(ScopesAllowed.class);
        if (scopesAllowedAnnotation == null) {
            return new AnnotationResult<>(method.getDeclaringClass().getDeclaredAnnotation(ScopesAllowed.class), true);
        } else {
            return new AnnotationResult<>(scopesAllowedAnnotation, false);
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
