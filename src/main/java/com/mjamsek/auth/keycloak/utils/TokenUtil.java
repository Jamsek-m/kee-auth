package com.mjamsek.auth.keycloak.utils;

public class TokenUtil {
    
    public static String trimAuthHeader(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }
        
        if (authorizationHeader.startsWith("Bearer")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        
        return authorizationHeader;
    }
    
}
