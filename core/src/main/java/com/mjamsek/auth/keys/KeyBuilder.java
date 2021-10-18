package com.mjamsek.auth.keys;

import com.mjamsek.auth.utils.CertUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;

import static com.mjamsek.auth.common.config.KeyConstants.*;

public class KeyBuilder {
    
    private final KeyEntry keyEntry;
    
    public static KeyBuilder newBuilder(String kid) {
        return new KeyBuilder(kid);
    }
    
    private KeyBuilder(String kid) {
        this.keyEntry = new KeyEntry(kid);
    }
    
    public HmacKeyBuilder withHmacAlgorithm(JWSAlgorithm algorithm) {
        if (JWSAlgorithm.Family.HMAC_SHA.contains(algorithm)) {
            return new HmacKeyBuilder(keyEntry, algorithm);
        }
        throw new RuntimeException("Invalid alg!");
    }
    
    public RsaKeyBuilder withRsaAlgorithm(JWSAlgorithm algorithm) {
        if (JWSAlgorithm.Family.RSA.contains(algorithm)) {
            return new RsaKeyBuilder(keyEntry, algorithm);
        }
        throw new RuntimeException("Invalid alg!");
    }
    
    public ECKeyBuilder withECAlgorithm(JWSAlgorithm algorithm) {
        if (JWSAlgorithm.Family.EC.contains(algorithm)) {
            return new ECKeyBuilder(keyEntry, algorithm);
        }
        throw new RuntimeException("Invalid alg!");
    }
    
    
    public static class HmacKeyBuilder {
        
        private final KeyEntry keyEntry;
        
        HmacKeyBuilder(KeyEntry keyEntry, JWSAlgorithm algorithm) {
            this.keyEntry = keyEntry;
            this.keyEntry.setAlgorithm(algorithm);
        }
        
        public HmacKeyBuilder withSecret(String secret) {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), keyEntry.getAlgorithm().getName());
            keyEntry.setVerificationKey(secretKey);
            return this;
        }
        
        public KeyEntry build() {
            return keyEntry;
        }
        
    }
    
    public static class RsaKeyBuilder {
        
        private final KeyEntry keyEntry;
        
        RsaKeyBuilder(KeyEntry keyEntry, JWSAlgorithm algorithm) {
            this.keyEntry = keyEntry;
            this.keyEntry.setAlgorithm(algorithm);
        }
        
        public RsaKeyBuilder withModulusAndExponent(String modulus, String exponent) {
            RSAKey rsaKey = new RSAKey.Builder(new Base64URL(modulus), new Base64URL(exponent)).build();
            try {
                PublicKey publicKey = rsaKey.toPublicKey();
                keyEntry.setVerificationKey(publicKey);
                return this;
            } catch (JOSEException e) {
                throw new RuntimeException("Invalid key spec!");
            }
        }
        
        public RsaKeyBuilder withPublicKey(String publicKey) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(RSA_FACTORY_KEY);
                PublicKey constructedPublicKey = CertUtil.createPublicKey(publicKey, keyFactory);
                keyEntry.setVerificationKey(constructedPublicKey);
                return this;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Invalid key spec!");
            }
        }
        
        public RsaKeyBuilder withX509Certificate(String x5c) {
            byte[] x5cBytes = Base64.getDecoder().decode(CertUtil.decodeX509CertificateChain(x5c));
            InputStream x5cInputStream = new ByteArrayInputStream(x5cBytes);
            try {
                Certificate certificate = CertificateFactory.getInstance(CERT_FACTORY_KEY).generateCertificate(x5cInputStream);
                PublicKey publicKey = certificate.getPublicKey();
                keyEntry.setVerificationKey(publicKey);
                return this;
            } catch (CertificateException e) {
                throw new RuntimeException("Invalid key spec!");
            }
        }
        
        public KeyEntry build() {
            return keyEntry;
        }
    }
    
    public static class ECKeyBuilder {
        
        private final KeyEntry keyEntry;
        
        ECKeyBuilder(KeyEntry keyEntry, JWSAlgorithm algorithm) {
            this.keyEntry = keyEntry;
            this.keyEntry.setAlgorithm(algorithm);
        }
        
        public ECKeyBuilder withCurveParameters(String crv, String x, String y) {
            Curve curve = Curve.parse(crv);
            ECKey ecKey = new ECKey.Builder(curve, new Base64URL(x), new Base64URL(y)).build();
            try {
                PublicKey publicKey = ecKey.toPublicKey();
                keyEntry.setVerificationKey(publicKey);
                return this;
            } catch (JOSEException e) {
                throw new RuntimeException("Invalid key spec!");
            }
        }
        
        public ECKeyBuilder withPublicKey(String publicKey) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(EC_FACTORY_KEY);
                PublicKey constructedPublicKey = CertUtil.createPublicKey(publicKey, keyFactory);
                keyEntry.setVerificationKey(constructedPublicKey);
                return this;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Invalid key spec!");
            }
        }
        
        public KeyEntry build() {
            return keyEntry;
        }
        
    }
}
