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
package com.mjamsek.auth;

import com.mjamsek.auth.common.exceptions.HttpCallException;
import com.mjamsek.auth.common.exceptions.MissingConfigException;
import com.mjamsek.auth.config.KeeAuthInitializator;
import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.producers.ContextProducer;

import javax.ws.rs.NotAuthorizedException;
import java.util.Map;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
@SuppressWarnings("unused")
public class KeeAuth {
    
    /**
     * Produce context from given jwt
     * @param jwt json web token
     * @return context instance containing claims from given jwt, or empty context if jwt is not valid
     */
    public static AuthContext createContext(String jwt) {
        if (jwt == null) {
            return ContextProducer.produceEmptyContext();
        }
        return ContextProducer.produceContext(jwt);
    }
    
    /**
     * Parses jwt to claims map
     * @param jwt json web token
     * @return parsed claims stored in a map
     * @throws NotAuthorizedException if jwt is not valid (i.e. expired)
     */
    public static Map<String, Object> parseJsonWebToken(String jwt) throws NotAuthorizedException {
        AuthContext authContext = ContextProducer.produceContext(jwt);
        if (authContext.isAuthenticated()) {
            return authContext.getTokenPayload();
        }
        throw new NotAuthorizedException("Invalid JWT!");
    }
    
    /**
     * Reloads well-known endpoint and updates configuration
     * @throws MissingConfigException If no well-known endpoint was provided
     * @throws HttpCallException If call to well-known endpoint fails for any reason
     */
    public static void reloadWellKnownConfig() throws MissingConfigException, HttpCallException {
        KeeAuthInitializator.loadWellKnownConfig();
    }
    
    /**
     * Reloads JWKS and updates configuration
     * @throws MissingConfigException If no jwks endpoint was provided
     * @throws HttpCallException If call to jwks endpoint fails for any reason
     */
    public static void reloadJwks() throws MissingConfigException, HttpCallException {
        KeeAuthInitializator.loadJwks();
    }
}
