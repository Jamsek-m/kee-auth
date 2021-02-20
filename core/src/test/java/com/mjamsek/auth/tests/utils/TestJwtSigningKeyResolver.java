package com.mjamsek.auth.tests.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import javax.crypto.SecretKey;
import java.security.Key;

public class TestJwtSigningKeyResolver extends SigningKeyResolverAdapter {
    
    private final SecretKey secretKey;
    
    public TestJwtSigningKeyResolver(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
    
    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        return secretKey;
    }
}
