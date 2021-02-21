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
