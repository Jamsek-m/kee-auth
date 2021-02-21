package com.mjamsek.auth.utils;

import java.util.Base64;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class DecodeUtil {
    
    public static byte[] base64Decode(String base64) {
        base64 = base64.replaceAll("-", "+");
        base64 = base64.replaceAll("_", "/");
        switch (base64.length() % 4) // Pad with trailing '='s
        {
            case 0:
                break; // No pad chars in this case
            case 2:
                base64 += "==";
                break; // Two pad chars
            case 3:
                base64 += "=";
                break; // One pad char
            default:
                throw new RuntimeException(
                    "Illegal base64url string!");
        }
        return Base64.getDecoder().decode(base64);
    }
    
}
