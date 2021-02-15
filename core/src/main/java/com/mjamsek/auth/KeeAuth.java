package com.mjamsek.auth;

import com.mjamsek.auth.config.KeeAuthInitializator;
import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.producers.ContextProducer;
import com.mjamsek.auth.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

public class KeeAuth {
    
    public static AuthContext createContext(String jwt) {
        if (jwt == null) {
            return ContextProducer.produceEmptyContext();
        }
        return ContextProducer.produceContext(jwt);
    }
    
    public static Jws<Claims> parseJsonWebToken(String jwt) throws JwtException {
        return TokenUtil.parseJwt(jwt);
    }
    
    public static void reloadWellKnownConfig() {
        KeeAuthInitializator.loadWellKnownConfig();
    }
    
    public static void reloadJwks() {
        KeeAuthInitializator.loadJwks();
    }
}
