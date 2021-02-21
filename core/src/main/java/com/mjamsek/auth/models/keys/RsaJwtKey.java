package com.mjamsek.auth.models.keys;

import com.mjamsek.auth.utils.DecodeUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class RsaJwtKey extends JwtSigningKey {
    
    protected final PublicKey publicKey;
    
    public RsaJwtKey(String kid, String n, String e) throws InvalidKeySpecException {
        super(kid);
        if (n == null || n.isEmpty()) {
            throw new InvalidKeySpecException("Missing modulus (n) parameter! Cannot construct public key!");
        }
        if (e == null || e.isEmpty()) {
            throw new InvalidKeySpecException("Missing exponent (e) parameter! Cannot construct public key!");
        }
        
        BigInteger modulus = new BigInteger(1, DecodeUtil.base64Decode(n));
        BigInteger exponent = new BigInteger(1, DecodeUtil.base64Decode(e));
        
        PublicKey publicKey;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (NoSuchAlgorithmException ignored) {
            publicKey = null;
        }
        this.publicKey = publicKey;
    }
    
    public RsaJwtKey(String kid, String x5c) throws InvalidKeySpecException {
        super(kid);
        
        if (x5c == null || x5c.isBlank()) {
            throw new InvalidKeySpecException("Missing certificate (x5c) parameter! Cannot construct public key!");
        }
        
        PublicKey publicKey;
        try {
            
            // remove header and footer
            x5c = x5c.replaceAll("-+BEGIN PUBLIC KEY-+", "");
            x5c = x5c.replaceAll("-+END PUBLIC KEY-+", "");
            // remove all non base64 characters
            x5c = x5c.replaceAll("[^A-Za-z0-9+/=]", "");
            
            byte[] x5cBytes = Base64.getDecoder().decode(x5c);
            InputStream x5cInputStream = new ByteArrayInputStream(x5cBytes);
            Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(x5cInputStream);
            
            publicKey = certificate.getPublicKey();
        } catch (CertificateException ignored) {
            publicKey = null;
        }
        this.publicKey = publicKey;
    }
    
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
}
