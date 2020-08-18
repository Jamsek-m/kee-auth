package com.mjamsek.auth.keycloak.client;

import com.mjamsek.auth.keycloak.apis.AuthenticationExceptionMapper;
import com.mjamsek.auth.keycloak.apis.GenericExceptionMapper;
import com.mjamsek.auth.keycloak.apis.KeycloakApi;
import com.mjamsek.auth.keycloak.config.KeycloakConfig;
import com.mjamsek.auth.keycloak.exceptions.KeycloakCallException;
import com.mjamsek.auth.keycloak.exceptions.KeycloakConfigException;
import com.mjamsek.auth.keycloak.models.TokenResponse;
import com.mjamsek.auth.keycloak.payload.KeycloakJsonWebToken;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.keycloak.common.VerificationException;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class KeycloakClient {
    
    private static final Logger log = Logger.getLogger(KeycloakClient.class.getName());
    
    private static final AtomicReference<TokenRepresentation> tokenCache = new AtomicReference<>();
    
    /**
     * Method for performing service calls to Keycloak server
     *
     * @param func function that will perform service call. Receives one argument - raw service token
     * @param <T>  Return type of service call response
     * @return response of service call
     * @throws KeycloakCallException on failed service call
     */
    public static <T> T callKeycloak(Function<String, T> func) throws KeycloakCallException {
        // if no token present, retrieve one, otherwise use cached one
        if (tokenCache.get() == null) {
            getServiceToken();
        }
        // if token is expired
        if (!tokenIsActive()) {
            getServiceToken();
        }
        
        // call requested function
        try {
            return func.apply(tokenCache.get().rawToken);
        } catch (Exception e) {
            throw new KeycloakCallException("Error performing service call!", e);
        }
    }
    
    @SuppressWarnings("UnusedReturnValue")
    public static String getServiceToken() throws KeycloakCallException {
        if (KeycloakConfig.getInstance().getClientSecret() == null) {
            log.severe("Client secret not provided, cannot perform service call!");
            throw new KeycloakConfigException("Client secret not provided!");
        }
    
        // Check if token is cached
        if (tokenCache.get() != null) {
            // Check if token is expired
            if (tokenIsActive()) {
                return tokenCache.get().rawToken;
            } else {
                log.fine("Stored service token is expired, retrieving new one.");
            }
        } else {
            log.fine("Client has no cached service token, retrieving new one.");
        }
        
        // If there is no cached valid token, retrieve new one
        try {
            KeycloakApi api = RestClientBuilder
                .newBuilder()
                .baseUri(URI.create(KeycloakConfig.getInstance().getAuthUrl()))
                .register(AuthenticationExceptionMapper.class)
                .register(GenericExceptionMapper.class)
                .build(KeycloakApi.class);
        
            TokenResponse response = api.getServiceToken(
                KeycloakConfig.getInstance().getRealm(),
                KeycloakConfig.ServiceCall.getFormData()
            );
            log.log(Level.INFO, "Retrieved service token for confidential client ''{0}''", KeycloakConfig.getInstance().getClientId());
            
            // Store received token in cache
            tokenCache.set(new TokenRepresentation(response.getAccessToken()));
            
            return response.getAccessToken();
        } catch (KeycloakCallException e) {
            log.severe(e.getMessage());
            throw e;
        } catch (VerificationException e) {
            log.severe(e.getMessage());
            throw new KeycloakCallException("Error verifying received service token!", e);
        }
    }
    
    private static boolean tokenIsActive() {
        KeycloakJsonWebToken jsonWebToken = tokenCache.get().parsedToken;
        return jsonWebToken.isActive(KeycloakConfig.getInstance().getLeeway());
    }
    
    static class TokenRepresentation {
        String rawToken;
        KeycloakJsonWebToken parsedToken;
        
        TokenRepresentation(String token) throws VerificationException {
            this.rawToken = token;
            this.parsedToken = KeycloakConfig.getInstance().getVerifier().verifyToken(token, KeycloakJsonWebToken.class);
        }
    }
}
