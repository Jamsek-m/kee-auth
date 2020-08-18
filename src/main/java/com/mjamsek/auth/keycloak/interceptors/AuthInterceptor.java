package com.mjamsek.auth.keycloak.interceptors;

import com.mjamsek.auth.keycloak.annotations.SecureResource;
import com.mjamsek.auth.keycloak.services.AuthService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.Priorities;


@SecureResource
@Interceptor
@Priority(Priorities.AUTHORIZATION)
public class AuthInterceptor {
    
    @Inject
    private AuthService authService;
    
    @AroundInvoke
    public Object authorize(InvocationContext context) throws Exception {
        authService.processSecurity(context);
        return context.proceed();
    }
    
}

