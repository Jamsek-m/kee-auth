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
