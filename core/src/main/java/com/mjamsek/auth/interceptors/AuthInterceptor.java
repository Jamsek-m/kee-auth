package com.mjamsek.auth.interceptors;

import com.mjamsek.auth.common.annotations.SecureResource;
import com.mjamsek.auth.services.KeeAuthService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.Priorities;

@SecureResource
@Interceptor
@Priority(Priorities.AUTHENTICATION)
public class AuthInterceptor {
    
    @Inject
    private KeeAuthService keeAuthService;
    
    @AroundInvoke
    public Object authenticate(InvocationContext context) throws Exception {
        keeAuthService.processSecurity(context);
        return context.proceed();
    }
    
}
