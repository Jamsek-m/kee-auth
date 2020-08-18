package com.mjamsek.auth.keycloak.utils;

import org.keycloak.crypto.Algorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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

public class CertUtil {
    
    public static SecretKey getSecretKeyFromString(String secretKeyString) {
        return new SecretKeySpec(secretKeyString.getBytes(), Algorithm.HS256);
    }
    
    public static PublicKey getPublicKeyFromCertificate(String certString) {
        try {
    
            // remove header and footer
            certString = certString.replaceAll("-+BEGIN PUBLIC KEY-+", "");
            certString = certString.replaceAll("-+END PUBLIC KEY-+", "");
            // remove all non base64 characters
            certString = certString.replaceAll("[^A-Za-z0-9+/=]", "");
            
            byte[] certder = Base64.getDecoder().decode(certString);
            InputStream certstream = new ByteArrayInputStream(certder);
            Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(certstream);
            return cert.getPublicKey();
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static PublicKey getPublicKeyFromModulusAndExponent(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating key!");
        }
    }
    
}
