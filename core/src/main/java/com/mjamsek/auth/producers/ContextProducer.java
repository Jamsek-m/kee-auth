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
package com.mjamsek.auth.producers;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class ContextProducer {
    
    private static final Logger LOG = Logger.getLogger(ContextProducer.class.getName());
    
    private static final String CLAIM_KEY_ID = "id";
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_EMAIL = "email";
    private static final String CLAIM_KEY_SCOPE = "scope";
    
    /**
     * Produce context from given jwt
     * @param jwt json web token
     * @return context instance containing claims from given jwt
     */
    public static AuthContext produceContext(String jwt) {
        try {
            Jws<Claims> tokenClaims = TokenUtil.parseJwt(jwt);
            Claims claims = tokenClaims.getBody();
            
            AuthContext.Builder contextBuilder = AuthContext.Builder.newBuilder();
            contextBuilder.authenticated(true);
            contextBuilder.token(jwt);
            contextBuilder.payload(claims);
            
            Map<String, String> claimMappings = getClaimMappings();
            contextBuilder.id(claims.get(claimMappings.get(CLAIM_KEY_ID), String.class));
            contextBuilder.email(claims.get(claimMappings.get(CLAIM_KEY_EMAIL), String.class));
            contextBuilder.username(claims.get(claimMappings.get(CLAIM_KEY_USERNAME), String.class));
            contextBuilder.scope(claims.get(claimMappings.get(CLAIM_KEY_SCOPE), String.class));
            
            return contextBuilder.build();
        } catch (JwtException e) {
            LOG.fine("Error verifying JWT: " + e.getMessage());
            return AuthContext.Builder.newEmptyContext();
        }
    }
    
    private static Map<String, String> getClaimMappings() {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        Map<String, String> mappings = new HashMap<>();
        mappings.put(CLAIM_KEY_ID, configUtil.get(ConfigKeys.CLAIM_MAPPING_ID).orElse("sub"));
        mappings.put(CLAIM_KEY_USERNAME, configUtil.get(ConfigKeys.CLAIM_MAPPING_USERNAME).orElse("preferred_username"));
        mappings.put(CLAIM_KEY_EMAIL, configUtil.get(ConfigKeys.CLAIM_MAPPING_EMAIL).orElse("email"));
        mappings.put(CLAIM_KEY_SCOPE, configUtil.get(ConfigKeys.CLAIM_MAPPING_SCOPE).orElse("scope"));
        return mappings;
    }
    
    /**
     * Produces empty context (unauthenticated user)
     * @return context instance with authenticated flag set to false
     */
    public static AuthContext produceEmptyContext() {
        return AuthContext.Builder.newEmptyContext();
    }
    
}
