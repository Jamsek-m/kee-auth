package com.mjamsek.auth.keycloak.verifiers;

import com.mjamsek.auth.keycloak.config.KeycloakConfig;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.JsonWebToken;

import javax.crypto.SecretKey;

public class HMACVerifier <T extends JsonWebToken> implements TokenVerifier<T> {
    
    @SuppressWarnings("unchecked")
    @Override
    public T verifyToken(String token, Class<T> tokenType) throws VerificationException {
        SecretKey secretKey = KeycloakConfig.getInstance().getSecretKey();
        
        org.keycloak.TokenVerifier<T> verifier = org.keycloak.TokenVerifier
            .create(token, tokenType)
            .withChecks(org.keycloak.TokenVerifier.SUBJECT_EXISTS_CHECK, org.keycloak.TokenVerifier.IS_ACTIVE)
            .secretKey(secretKey);
    
        return verifier.verify().getToken();
    }
}
