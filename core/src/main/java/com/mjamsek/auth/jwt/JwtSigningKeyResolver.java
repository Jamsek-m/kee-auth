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
