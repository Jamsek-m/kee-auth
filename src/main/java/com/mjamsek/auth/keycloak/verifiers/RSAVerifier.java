package com.mjamsek.auth.keycloak.verifiers;

import com.mjamsek.auth.keycloak.config.KeycloakConfig;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.JsonWebToken;

import java.security.PublicKey;

public class RSAVerifier <T extends JsonWebToken> implements TokenVerifier<T> {
    
    @SuppressWarnings("unchecked")
    @Override
    public T verifyToken(String token, Class<T> tokenType) throws VerificationException {
        PublicKey publicKey = KeycloakConfig.getInstance().getPublicKey();
        
        org.keycloak.TokenVerifier<T> verifier = org.keycloak.TokenVerifier
            .create(token, tokenType)
            .withChecks(org.keycloak.TokenVerifier.SUBJECT_EXISTS_CHECK, org.keycloak.TokenVerifier.IS_ACTIVE)
            .publicKey(publicKey);
    
        return verifier.verify().getToken();
    }
}
