package com.mjamsek.auth.models.keys;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class EllipticCurveJwtKey extends JwtSigningKey {
    
    private PublicKey publicKey;
    
    public EllipticCurveJwtKey(String kid, String curve, String x, String y) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        super(kid);
    }
    
}
