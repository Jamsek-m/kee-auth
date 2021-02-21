package com.mjamsek.auth;

import com.mjamsek.auth.config.KeeAuthInitializator;
import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.producers.ContextProducer;
import com.mjamsek.auth.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

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
     * @throws JwtException if jwt is not valid (i.e. expired)
     */
    public static Jws<Claims> parseJsonWebToken(String jwt) throws JwtException {
        return TokenUtil.parseJwt(jwt);
    }
    
    /**
     * Reloads well-known endpoint and updates configuration
     */
    public static void reloadWellKnownConfig() {
        KeeAuthInitializator.loadWellKnownConfig();
    }
    
    /**
     * Reloads JWKS and updates configuration
     */
    public static void reloadJwks() {
        KeeAuthInitializator.loadJwks();
    }
}
