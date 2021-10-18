/*
 *  Copyright (c) 2019-2021 Miha Jamsek and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.mjamsek.auth.apis;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.config.ConfigDefaults;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.common.exceptions.HttpCallException;
import com.mjamsek.auth.common.exceptions.MissingConfigException;
import com.mjamsek.auth.config.KeeAuthConfig;
import com.mjamsek.auth.models.TokenResponse;
import com.mjamsek.auth.models.WellKnownConfig;
import com.nimbusds.jose.jwk.JWKSet;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;

import static com.mjamsek.auth.common.config.OIDCConstants.*;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class IdentityProviderApi {
    
    private static final Logger LOG = Logger.getLogger(IdentityProviderApi.class.getName());
    
    public static TokenResponse getTokens() throws MissingConfigException, HttpCallException {
        String tokenEndpoint = getTokenEndpoint();
        Form formData = new Form(GRANT_TYPE_PARAM, GRANT_TYPE_CLIENT_CREDENTIALS_VALUE);
        String encodedCredentials = getEncodedCredentials();
        
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
    
    private static String getEncodedCredentials() throws MissingConfigException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        String clientId = configUtil.get(ConfigKeys.Oidc.ClientCredentials.CLIENT_ID)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.Oidc.ClientCredentials.CLIENT_ID));
        String clientSecret = configUtil.get(ConfigKeys.Oidc.ClientCredentials.CLIENT_SECRET)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.Oidc.ClientCredentials.CLIENT_SECRET));
        
        return new String(Base64.getEncoder().encode(
            (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)));
    }
    
    public static WellKnownConfig getWellKnownConfig() throws MissingConfigException, HttpCallException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        String wellKnownEndpoint = configUtil.get(ConfigKeys.Oidc.WELL_KNOWN_URL)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.Oidc.WELL_KNOWN_URL));
        
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
    
    public static JWKSet getJWKS() throws MissingConfigException, HttpCallException, ParseException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        String jwksEndpoint = configUtil.get(ConfigKeys.Oidc.JWKS_URL)
            .or(KeeAuthConfig::getJwksEndpoint)
            .orElseThrow(() -> new MissingConfigException(ConfigKeys.Oidc.JWKS_URL));
        
        Response response = ClientBuilder.newClient()
            .target(jwksEndpoint)
            .request(MediaType.APPLICATION_JSON)
            .get();
        
        if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
            String errorResponse = response.readEntity(String.class);
            LOG.severe("Error fetching JWKS! Received response: " + errorResponse);
            throw new HttpCallException("Unable to retrieve JWKS!");
        } else {
            String responsePayload = response.readEntity(String.class);
            return JWKSet.parse(responsePayload);
        }
    }
    
    private static String getTokenEndpoint() {
        return ConfigurationUtil.getInstance()
            .get(ConfigKeys.Oidc.ClientCredentials.TOKEN_URL)
            .or(() -> {
                if (!ConfigDefaults.autoconfigurationEnabled()) {
                    return Optional.empty();
                }
                return KeeAuthConfig.getTokenEndpoint();
            }).orElseThrow(() -> new MissingConfigException(ConfigKeys.Oidc.ClientCredentials.TOKEN_URL));
    }
    
}
