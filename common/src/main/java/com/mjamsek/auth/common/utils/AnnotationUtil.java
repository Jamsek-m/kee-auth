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
package com.mjamsek.auth.common.utils;


import com.mjamsek.auth.common.annotations.AuthenticatedAllowed;
import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.annotations.ScopesAllowed;
import com.mjamsek.auth.common.models.AnnotationResult;

import java.lang.reflect.Method;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class AnnotationUtil {
    
    private AnnotationUtil() {
    
    }
    
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
