package com.mjamsek.auth.utils;

import com.nimbusds.jose.util.Base64URL;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CertUtil {
    
    private CertUtil() {
    
    }
    
    public static String decodeX509CertificateChain(String x5c) {
        // remove header and footer, if present
        x5c = x5c.replaceAll("-+BEGIN PUBLIC KEY-+", "");
        x5c = x5c.replaceAll("-+END PUBLIC KEY-+", "");
        // remove all non base64 characters
        return Base64URL.from(x5c).toString();
    }
    
    public static PublicKey createPublicKey(String certificate, KeyFactory keyFactory) {
        byte[] publicKeyBytes = Base64.getDecoder().decode(certificate.getBytes(StandardCharsets.UTF_8));
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid key spec!");
        }
    }
}
