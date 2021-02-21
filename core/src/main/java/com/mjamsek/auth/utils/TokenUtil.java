package com.mjamsek.auth.utils;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.jwt.JwtSigningKeyResolver;
import io.jsonwebtoken.*;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class TokenUtil {
    
    public static String trimAuthorizationHeader(String authorizationHeaderValue) {
        if (authorizationHeaderValue == null) {
            return null;
        }
        
        if (authorizationHeaderValue.startsWith("Bearer ")) {
            return authorizationHeaderValue.replace("Bearer ", "");
        }
        
        return authorizationHeaderValue;
    }
    
    public static Jws<Claims> parseJwt(String jwt) throws JwtException {
        SigningKeyResolver keyResolver = new JwtSigningKeyResolver();
        return TokenUtil.parseJwt(jwt, keyResolver);
    }
    
    public static Jws<Claims> parseJwt(String jwt, SigningKeyResolver keyResolver) throws JwtException {
        long tokenLeeway = ConfigurationUtil.getInstance()
            .getInteger(ConfigKeys.JWT_TIME_LEEWAY)
            .orElse(1);
        
        JwtParser parser = Jwts.parserBuilder()
            .setAllowedClockSkewSeconds(tokenLeeway)
            .setSigningKeyResolver(keyResolver)
            .build();
        
        return parser.parseClaimsJws(jwt);
    }
    
}
