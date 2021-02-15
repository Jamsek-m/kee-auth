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

public class IdentityProviderClient {
    
    private static final Logger LOG = Logger.getLogger(IdentityProviderClient.class.getName());
    
    private static final AtomicReference<TokenCache> accessTokenCache = new AtomicReference<>();
    
    public static <T> T call(Function<String, T> callFunction) throws HttpCallException {
        TokenCache tokenCache = accessTokenCache.get();
        if (tokenCache == null) {
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
