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
package com.mjamsek.auth.jwt;

import com.mjamsek.auth.common.exceptions.UnrecognizedKeyException;
import com.mjamsek.auth.config.KeeAuthConfig;
import com.mjamsek.auth.keys.KeyLoader;
import com.mjamsek.auth.models.keys.HmacJwtKey;
import com.mjamsek.auth.models.keys.JwtSigningKey;
import com.mjamsek.auth.models.keys.RsaJwtKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import java.security.Key;
import java.util.Map;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class JwtSigningKeyResolver extends SigningKeyResolverAdapter {
    
    // TODO: client error needs to throw 401/403
    
    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        Map<String, JwtSigningKey> signingKeys = loadKeys();
        
        JwtSigningKey key = signingKeys.get(header.getKeyId());
        if (key == null) {
            throw new UnrecognizedKeyException("Unrecognized key id! Token is signed with unregistered key!");
        }
        
        if (key instanceof RsaJwtKey) {
            return ((RsaJwtKey) key).getPublicKey();
        } else if (key instanceof HmacJwtKey) {
            return ((HmacJwtKey) key).getSecretKey();
        } else {
            throw new UnrecognizedKeyException("Unsupported signing algorithm! Token is signed with unsupported signing algorithm!");
        }
    }
    
    private Map<String, JwtSigningKey> loadKeys() {
        // Initialize with keys retrieved from jwks url
        Map<String, JwtSigningKey> keys = KeeAuthConfig.getSigningKeys();
        
        // Insert locally provided keys, key with same kid is overridden
        KeyLoader.loadKeys(KeyLoader.readKeysFromConfiguration()).forEach(key -> {
            keys.put(key.getKid(), key);
        });
        
        return keys;
    }
}
