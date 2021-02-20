package com.mjamsek.auth.tests.tests.keys;

import com.mjamsek.auth.models.keys.RsaJwtKey;
import com.mjamsek.auth.utils.DecodeUtil;
import io.jsonwebtoken.security.Keys;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Arquillian.class)
public class RsaJwtKeyTest {
    
    private static final String KID = "abcde123";
    private static final String CERT = "MIIClTCCAX0CBgF3ISs0kjANBgkqhkiG9w0BAQsFADAOMQwwCgYDVQQDDANrcnYwHhcNMjEwMTIwMTg1ODM1WhcNMzEwMTIwMTkwMDE1WjAOMQwwCgYDVQQDDANrcnYwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCq3OOPcq4WV2sMveukiwqgSPPHAdARSbNA0egI/wJiID7b2r5Oq3yzQAuDmu4r5QfmkEnG2LES5gbSOQ1wcUs58nPjlbYtzWSNi05BuMwQN7CsIbakMNP6QFuOz8Zpo4g1B9Xl7UO6/0FikdJpC2uKf7J45fgu7lTqpNHO5Kj5s7b10HpWvkuPy+ERSbm9AT1Ha+wzVnJfAZXosvCL5w4VFl8SEqAeF+hTMa5moHgQjvgujI/K3FMOC4De1LPZ5xwAuNN1+ivTc1RIJ32qI4LXUUbDSBW16imWRbucfp1UzB9N8HIZvp9PNjLZjH/fSQI7rI3AAtB0O6Mk3GzZiffbAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAKAT+e2wVNEJld5CCtpqN13fvCJw7Yc2HZdeg648JqkrLOVyZltynMA2VG6NM4sDwQcH3Cb75nPcfND+rUrTSDiu0eQ3xfCh4pfsqvh8EEQ95yrGGm91McdGlNt24IUZNfGFzGZs/cCfuxoQUfpMCSSlVN/6SFpm8E5wHMYP9ALR+Aw8eVWMOOpDDzQWDZHo7lxkz4rMuaeWqOggAw+rMeOEblpWoU52Mxy02x+I8GUJdxjwL1atRI1yuUp7LkZ1O+NTtVmB0tdOvHpUlDGRPIbF2VfeurrIIs0y2Pz1TdCR5eSxjVHPleiabaKjWft8qUysGGptTr+FmGj9KG8h3L8=";
    private static final String N = "qtzjj3KuFldrDL3rpIsKoEjzxwHQEUmzQNHoCP8CYiA-29q-Tqt8s0ALg5ruK-UH5pBJxtixEuYG0jkNcHFLOfJz45W2Lc1kjYtOQbjMEDewrCG2pDDT-kBbjs_GaaOINQfV5e1Duv9BYpHSaQtrin-yeOX4Lu5U6qTRzuSo-bO29dB6Vr5Lj8vhEUm5vQE9R2vsM1ZyXwGV6LLwi-cOFRZfEhKgHhfoUzGuZqB4EI74LoyPytxTDguA3tSz2eccALjTdfor03NUSCd9qiOC11FGw0gVteoplkW7nH6dVMwfTfByGb6fTzYy2Yx_30kCO6yNwALQdDujJNxs2Yn32w";
    private static final String E = "AQAB";
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(RsaJwtKey.class)
            .addClass(Keys.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void createFromCert() throws InvalidKeySpecException, CertificateException {
        RsaJwtKey rsaJwtKey = new RsaJwtKey(KID, CERT);
        
        assertEquals(KID, rsaJwtKey.getKid());
        
        // Need to assert validity/viability of this test
        RSAPublicKey rsaPublicKey = (RSAPublicKey) rsaJwtKey.getPublicKey();
        String x5c = CERT;
        x5c = x5c.replaceAll("-+BEGIN PUBLIC KEY-+", "");
        x5c = x5c.replaceAll("-+END PUBLIC KEY-+", "");
        x5c = x5c.replaceAll("[^A-Za-z0-9+/=]", "");
        byte[] x5cBytes = Base64.getDecoder().decode(x5c);
        InputStream x5cInputStream = new ByteArrayInputStream(x5cBytes);
        Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(x5cInputStream);
        PublicKey pubKeyFromCert = certificate.getPublicKey();
        
        assertEquals(new String(pubKeyFromCert.getEncoded()), new String(rsaPublicKey.getEncoded()));
    }
    
    @Test
    public void createFromModulus() throws InvalidKeySpecException {
        BigInteger modulus = new BigInteger(1, DecodeUtil.base64Decode(N));
        BigInteger exponent = new BigInteger(1, DecodeUtil.base64Decode(E));
        
        RsaJwtKey rsaJwtKey = new RsaJwtKey(KID, N, E);
        assertEquals(KID, rsaJwtKey.getKid());
        
        RSAPublicKey rsaPublicKey = (RSAPublicKey) rsaJwtKey.getPublicKey();
        String keyModulus = rsaPublicKey.getModulus().toString(10);
        String keyExponent = rsaPublicKey.getPublicExponent().toString(10);
        
        assertEquals(modulus.toString(10), keyModulus);
        assertEquals(exponent.toString(10), keyExponent);
    }
    
    @Test
    public void missingKey() {
        assertThrows(InvalidKeySpecException.class, () -> new RsaJwtKey(KID, null));
        assertThrows(InvalidKeySpecException.class, () -> new RsaJwtKey(KID, ""));
        assertThrows(InvalidKeySpecException.class, () -> new RsaJwtKey(KID, "  "));
        
        assertThrows(InvalidKeySpecException.class, () -> new RsaJwtKey(KID, "  ", null));
        assertThrows(InvalidKeySpecException.class, () -> new RsaJwtKey(KID, null, null));
        assertThrows(InvalidKeySpecException.class, () -> new RsaJwtKey(KID, null, ""));
        assertThrows(InvalidKeySpecException.class, () -> new RsaJwtKey(KID, "", null));
        assertThrows(InvalidKeySpecException.class, () -> new RsaJwtKey(KID, "  ", ""));
    }
    
}
