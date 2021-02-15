package com.mjamsek.auth.services;

import javax.interceptor.InvocationContext;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;

public interface KeeAuthService {
    
    /**
     * Checks all applied constraints and if they are not satisfied throws an exception
     * @param context interceptor invocation context
     * @throws NotAuthorizedException when authentication is required and invalid credentials (or lack thereof) are presented.
     * @throws ForbiddenException when presented credentials are valid, but they lack additional rights (ie. no required role)
     */
    void processSecurity(InvocationContext context) throws NotAuthorizedException, ForbiddenException;
    
}
