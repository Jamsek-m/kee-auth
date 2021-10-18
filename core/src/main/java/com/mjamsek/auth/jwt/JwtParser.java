package com.mjamsek.auth.jwt;

import com.mjamsek.auth.keys.KeyEntry;
import com.mjamsek.auth.keys.KeyLoader;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

import javax.crypto.SecretKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

public class JwtParser {
    
    private final String rawToken;
    
    public JwtParser(String jwt) {
        this.rawToken = jwt;
    }
    
    public SignedJWT parse() throws ParseException {
        return SignedJWT.parse(this.rawToken);
    }
    
    public SignedJWT parseAndVerifyJwt() throws ParseException, JOSEException, RuntimeException {
        SignedJWT signedJWT = parse();
        JWSVerifier verifier = getKeyVerifier(signedJWT.getHeader().getKeyID());
        
        boolean validToken = signedJWT.verify(verifier);
        if (!validToken) {
            throw new RuntimeException("Invalid token!");
        }
        return signedJWT;
    }
    
    private JWSVerifier getKeyVerifier(String keyId) throws JOSEException {
        RuntimeException unrecognizedKeyException = new RuntimeException("Unrecognized key!");
        
        KeyEntry keyEntry = KeyLoader.getInstance().loadKey(keyId)
            .orElseThrow(() -> unrecognizedKeyException);
        
        if (JWSAlgorithm.Family.RSA.contains(keyEntry.getAlgorithm())) {
            if (keyEntry.getVerificationKey() instanceof RSAPublicKey) {
                return new RSASSAVerifier((RSAPublicKey) keyEntry.getVerificationKey());
            }
        } else if (JWSAlgorithm.Family.EC.contains(keyEntry.getAlgorithm())) {
            if (keyEntry.getVerificationKey() instanceof ECPublicKey) {
                return new ECDSAVerifier((ECPublicKey) keyEntry.getVerificationKey());
            }
        } else if (JWSAlgorithm.Family.HMAC_SHA.contains(keyEntry.getAlgorithm())) {
            if (keyEntry.getVerificationKey() instanceof SecretKey) {
                return new MACVerifier((SecretKey) keyEntry.getVerificationKey());
            }
        }
        
        throw unrecognizedKeyException;
    }
    
}
