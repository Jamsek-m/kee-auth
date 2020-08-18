package com.mjamsek.auth.keycloak.apis;

import com.mjamsek.auth.keycloak.exceptions.KeycloakCallException;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.annotation.Priority;
import javax.interceptor.Interceptor;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Priority(Interceptor.Priority.LIBRARY_BEFORE)
public class AuthenticationExceptionMapper implements ResponseExceptionMapper<KeycloakCallException> {
    
    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        return status == Response.Status.UNAUTHORIZED.getStatusCode() ||
            status == Response.Status.BAD_REQUEST.getStatusCode();
    }
    
    @Override
    public KeycloakCallException toThrowable(Response response) {
        if (response.hasEntity()) {
            KeycloakErrorResponse error = response.readEntity(KeycloakErrorResponse.class);
            if (error.getErrorDescription() != null && !error.getErrorDescription().isBlank()) {
                return new KeycloakCallException(error.getErrorDescription());
            }
            return new KeycloakCallException("Unknown Keycloak error! Response code: " + error.getErrorCode());
        }
        return new KeycloakCallException("Unknown Keycloak error! Response status: " + response.getStatus());
    }
}
