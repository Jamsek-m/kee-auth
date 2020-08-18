package com.mjamsek.auth.keycloak.exceptions;

public class KeycloakCallException extends KeycloakException {
    
    public KeycloakCallException(String message) {
        super(message);
    }
    
    public KeycloakCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
