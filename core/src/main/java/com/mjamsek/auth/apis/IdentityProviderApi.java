package com.mjamsek.auth.apis;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.common.exceptions.HttpCallException;
import com.mjamsek.auth.common.exceptions.MissingConfigException;
import com.mjamsek.auth.config.KeeAuthConfig;
import com.mjamsek.auth.models.JsonWebKeySet;
import com.mjamsek.auth.models.TokenResponse;
import com.mjamsek.auth.models.WellKnownConfig;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;


public class IdentityProviderApi {
    
    private static final Logger LOG = Logger.getLogger(IdentityProviderApi.class.getName());
    
    private static final String GRANT_TYPE_KEY = "grant_type";
    private static final String GRANT_TYPE_CLIENT_CREDENTIALS_VALUE = "client_credentials";
    private static final String AUTHORIZATION_BASIC_PREFIX = "Basic";
    
    public static TokenResponse getTokens() throws MissingConfigException, HttpCallException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        String tokenEndpoint = configUtil.get(ConfigKeys.TOKEN_URL)
            .or(KeeAuthConfig::getTokenEndpoint)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.TOKEN_URL));
        String clientId = configUtil.get(ConfigKeys.CLIENT_CREDENTIALS_ID)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.CLIENT_CREDENTIALS_ID));
        String clientSecret = configUtil.get(ConfigKeys.CLIENT_CREDENTIALS_SECRET)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.CLIENT_CREDENTIALS_SECRET));
        
        Form formData = new Form(GRANT_TYPE_KEY, GRANT_TYPE_CLIENT_CREDENTIALS_VALUE);
        String encodedCredentials = new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)));
        
        Response response = ClientBuilder.newClient()
            .target(tokenEndpoint)
            .request(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_BASIC_PREFIX + " " + encodedCredentials)
            .post(Entity.form(formData));
        
        if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
            String errorResponse = response.readEntity(String.class);
            LOG.severe("Error fetching configuration from .well-known endpoint! Received response: " + errorResponse);
            throw new HttpCallException("Unable to retrieve OIDC .well-known endpoint!");
        } else {
            return response.readEntity(TokenResponse.class);
        }
    }
    
    public static WellKnownConfig getWellKnownConfig() throws MissingConfigException, HttpCallException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        String wellKnownEndpoint = configUtil.get(ConfigKeys.WELL_KNOWN_URL)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.WELL_KNOWN_URL));
        
        Response response = ClientBuilder.newClient()
            .target(wellKnownEndpoint)
            .request(MediaType.APPLICATION_JSON)
            .get();
        
        if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
            String errorResponse = response.readEntity(String.class);
            LOG.severe("Error fetching configuration from .well-known endpoint! Received response: " + errorResponse);
            throw new HttpCallException("Unable to retrieve OIDC .well-known endpoint!");
        } else {
            return response.readEntity(WellKnownConfig.class);
        }
    }
    
    public static JsonWebKeySet getJsonWebKeySet() throws MissingConfigException, HttpCallException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        String jwksEndpoint = configUtil.get(ConfigKeys.JWKS_URL)
            .or(KeeAuthConfig::getJwksEndpoint)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.JWKS_URL));
        
        Response response = ClientBuilder.newClient()
            .target(jwksEndpoint)
            .request(MediaType.APPLICATION_JSON)
            .get();
        
        if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
            String errorResponse = response.readEntity(String.class);
            LOG.severe("Error fetching JWKS! Received response: " + errorResponse);
            throw new HttpCallException("Unable to retrieve JWKS!");
        } else {
            return response.readEntity(JsonWebKeySet.class);
        }
    }
    
}
