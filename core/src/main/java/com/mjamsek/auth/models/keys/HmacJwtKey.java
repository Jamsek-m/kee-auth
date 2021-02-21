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
