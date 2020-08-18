package com.mjamsek.auth.keycloak.verifiers;

import org.keycloak.common.VerificationException;
import org.keycloak.representations.JsonWebToken;

public interface TokenVerifier<T extends JsonWebToken> {
    
    T verifyToken(String token, Class<T> tokenType) throws VerificationException;
    
}
