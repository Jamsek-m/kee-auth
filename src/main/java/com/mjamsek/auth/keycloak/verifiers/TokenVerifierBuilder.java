package com.mjamsek.auth.keycloak.verifiers;

import com.mjamsek.auth.keycloak.enums.VerificationAlgorithm;
import org.keycloak.representations.JsonWebToken;

public class TokenVerifierBuilder {
    
    public static <T extends JsonWebToken> TokenVerifier<T> create(VerificationAlgorithm algorithm) {
        switch (algorithm) {
            case HS265:
                return new HMACVerifier<>();
            default:
            case RS265:
                return new RSAVerifier<>();
        }
    }
    
}
