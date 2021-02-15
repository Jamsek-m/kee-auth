package com.mjamsek.auth.models.keys;

import com.mjamsek.auth.common.enums.VerificationAlgorithm;
import com.mjamsek.auth.common.resolvers.ResolverDef;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class HmacJwtKey extends JwtSigningKey {
    
    private final SecretKey secretKey;
    
    public HmacJwtKey(String kid, VerificationAlgorithm algorithm, String secret) throws InvalidKeySpecException {
        super(kid);
    
        if (secret == null || secret.isBlank()) {
            throw new InvalidKeySpecException("Missing secret parameter! Cannot construct secret key!");
        }
        
        String algo = translateAlgorithm(algorithm);
        SecretKeySpec spec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algo);
        
        SecretKey secretKey;
        try {
            secretKey = SecretKeyFactory.getInstance(algo).generateSecret(spec);
        } catch (NoSuchAlgorithmException ignored) {
            secretKey = null;
        }
        this.secretKey = secretKey;
    }
    
    private String translateAlgorithm(VerificationAlgorithm algorithm) throws InvalidKeySpecException {
        switch (algorithm) {
            case HS512:
                return "HmacSHA512";
            case HS384:
                return "HmacSHA384";
            case HS256:
                return "HmacSHA256";
            default:
                throw new InvalidKeySpecException("Unsupported algorithm " + algorithm.name() + "!");
        }
    }
    
    public SecretKey getSecretKey() {
        return secretKey;
    }
    
}
