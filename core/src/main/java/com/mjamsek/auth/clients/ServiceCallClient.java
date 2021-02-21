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
package com.mjamsek.auth.clients;

import com.mjamsek.auth.apis.IdentityProviderApi;
import com.mjamsek.auth.common.exceptions.HttpCallException;
import com.mjamsek.auth.models.TokenResponse;
import com.mjamsek.auth.utils.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Client for client credentials flow
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class ServiceCallClient {
    
    private static final Logger LOG = Logger.getLogger(ServiceCallClient.class.getName());
    
    private static final AtomicReference<TokenCache> accessTokenCache = new AtomicReference<>();
    
    /**
     * Execute client credentials flow call, with provided service access token
     * @param callFunction function to be executed with credentials
     * @param <T> return type of a call result
     * @return returned data from call
     * @throws HttpCallException when exception had occurred during call (that may be exception in executed function or error response from called service.
     */
    @SuppressWarnings("unused")
    public static <T> T call(Function<String, T> callFunction) throws HttpCallException {
        TokenCache tokenCache = accessTokenCache.get();
        if (tokenCache == null) {
            LOG.finer("No token is stored in cache, retrieving new one.");
            tokenCache = retrieveToken();
            accessTokenCache.set(tokenCache);
        }
        if (tokenCache.isExpired()) {
            LOG.fine("Stored service token is expired, retrieving new one.");
            tokenCache = retrieveToken();
            accessTokenCache.set(tokenCache);
        }
        
        try {
            return callFunction.apply(tokenCache.getAccessToken());
        } catch (Exception e) {
            throw new HttpCallException("Error performing client credentials flow call!", e);
        }
    }
    
    private static TokenCache retrieveToken() throws HttpCallException {
        TokenResponse tokens = IdentityProviderApi.getTokens();
        return new TokenCache(tokens.getAccessToken());
    }
    
    private static class TokenCache {
        private final String accessToken;
        
        public TokenCache(String jwt) throws JwtException {
            this.accessToken = jwt;
        }
        
        public String getAccessToken() {
            return accessToken;
        }
        
        public boolean isExpired() {
            try {
                TokenUtil.parseJwt(this.accessToken);
                return false;
            } catch (ExpiredJwtException e) {
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
    
}
