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
package com.mjamsek.auth.models.keys;

import com.mjamsek.auth.common.enums.VerificationAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.spec.InvalidKeySpecException;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class HmacJwtKey extends JwtSigningKey {
    
    private final SecretKey secretKey;
    
    public HmacJwtKey(String kid, VerificationAlgorithm algorithm, String secret) throws InvalidKeySpecException, WeakKeyException {
        super(kid);
        
        if (secret == null || secret.isBlank()) {
            throw new InvalidKeySpecException("Missing secret parameter! Cannot construct secret key!");
        }
        
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    public SecretKey getSecretKey() {
        return secretKey;
    }
    
}
