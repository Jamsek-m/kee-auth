package com.mjamsek.auth.keycloak.apis;

import com.mjamsek.auth.keycloak.exceptions.KeycloakCallException;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.Response;


public class GenericExceptionMapper implements ResponseExceptionMapper<KeycloakCallException> {
    
    @Override
    public KeycloakCallException toThrowable(Response response) {
        if (response.hasEntity()) {
            KeycloakErrorResponse error = response.readEntity(KeycloakErrorResponse.class);
            if (error.getErrorDescription() != null && error.getErrorDescription().isBlank()) {
                return new KeycloakCallException(error.getErrorDescription());
            }
            return new KeycloakCallException("Unknown Keycloak error! Response code: " + error.getErrorCode());
        }
        return new KeycloakCallException("Unknown Keycloak error! Response status: " + response.getStatus());
    }
}
