package com.mjamsek.auth.keycloak.config;

import com.mjamsek.auth.keycloak.enums.VerificationAlgorithm;
import com.mjamsek.auth.keycloak.payload.KeycloakJsonWebToken;
import com.mjamsek.auth.keycloak.verifiers.TokenVerifier;

import javax.crypto.SecretKey;
import javax.ws.rs.core.Form;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Base64;

public class KeycloakConfig {
    
    private static KeycloakConfig instance;
    
    String realm;
    String authUrl;
    String clientId;
    
    VerificationAlgorithm algorithm;
    String clientSecret;
    SecretKey secretKey;
    PublicKey publicKey;
    Integer leeway;
    
    KeyMeta keyMeta;
    
    TokenVerifier<KeycloakJsonWebToken> verifier;
    
    /**
     * Retrieve keycloak config
     * @return object with configuration
     */
    public synchronized static KeycloakConfig getInstance() {
        if (instance == null) {
            instance = new KeycloakConfig();
        }
        return instance;
    }
    
    private KeycloakConfig() {
    
    }
    
    /**
     * Get realm id
     * @return keycloak realm
     */
    public synchronized String getRealm() {
        return realm;
    }
    
    /**
     * Get server url
     * @return keycloak server url
     */
    public synchronized String getAuthUrl() {
        return authUrl;
    }
    
    /**
     * Get client id
     * @return keycloak client
     */
    public synchronized String getClientId() {
        return clientId;
    }
    
    /**
     * Values retrieved from Keycloak server
     * @return public key meta data
     */
    public KeyMeta getKeyMeta() {
        return keyMeta;
    }
    
    /**
     * Get client secret
     * @return keycloak client secret
     */
    public synchronized String getClientSecret() {
        return clientSecret;
    }
    
    /**
     * Get signing algorithm
     * @return signing algorithm
     */
    public synchronized VerificationAlgorithm getAlgorithm() {
        return algorithm;
    }
    
    /**
     * Secret key for HS256 algorithm
     * @return secret key
     */
    public synchronized SecretKey getSecretKey() {
        return secretKey;
    }
    
    /**
     * Public key for RS256 algorithm
     * @return public key
     */
    public synchronized PublicKey getPublicKey() {
        return publicKey;
    }
    
    /**
     * Get token verifier instance
     * @return instance of token verifier for specified algorithm
     */
    public synchronized TokenVerifier<KeycloakJsonWebToken> getVerifier() {
        return verifier;
    }
    
    /**
     * Get leeway for token expiration check
     * @return num of milliseconds of leeway
     */
    public synchronized Integer getLeeway() {
        return leeway;
    }
    
    public static class KeyMeta {
        String kid;
        BigInteger modulus;
        BigInteger exponent;
        String cert;
    
        public String getKid() {
            return kid;
        }
    
        public BigInteger getModulus() {
            return modulus;
        }
    
        public BigInteger getExponent() {
            return exponent;
        }
        
        public String getCert() { return cert; }
        
    }
    
    public static class ServiceCall {
    
        public static Form getFormData() {
            Form formData = new Form();
            formData.param("grant_type", "client_credentials");
            return formData;
        }
    
        public static String getAuthHeader() {
            KeycloakConfig config = KeycloakConfig.getInstance();
            String credentials = config.clientId + ":" + config.clientSecret;
            String credentialsEncoded = new String(Base64.getEncoder().encode(credentials.getBytes()));
            return "Basic " + credentialsEncoded;
        }
    }
}
