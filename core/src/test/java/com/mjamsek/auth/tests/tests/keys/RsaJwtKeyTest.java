package com.mjamsek.auth.tests.tests.keys;

import com.mjamsek.auth.keys.KeyBuilder;
import com.mjamsek.auth.keys.KeyEntry;
import com.nimbusds.jose.JWSAlgorithm;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class RsaJwtKeyTest {
    
    private static final String KID = "abcde123";
    private static final String CERT = "MIIClTCCAX0CBgF3ISs0kjANBgkqhkiG9w0BAQsFADAOMQwwCgYDVQQDDANrcnYwHhcNMjEwMTIwMTg1ODM1WhcNMzEwMTIwMTkwMDE1WjAOMQwwCgYDVQQDDANrcnYwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCq3OOPcq4WV2sMveukiwqgSPPHAdARSbNA0egI/wJiID7b2r5Oq3yzQAuDmu4r5QfmkEnG2LES5gbSOQ1wcUs58nPjlbYtzWSNi05BuMwQN7CsIbakMNP6QFuOz8Zpo4g1B9Xl7UO6/0FikdJpC2uKf7J45fgu7lTqpNHO5Kj5s7b10HpWvkuPy+ERSbm9AT1Ha+wzVnJfAZXosvCL5w4VFl8SEqAeF+hTMa5moHgQjvgujI/K3FMOC4De1LPZ5xwAuNN1+ivTc1RIJ32qI4LXUUbDSBW16imWRbucfp1UzB9N8HIZvp9PNjLZjH/fSQI7rI3AAtB0O6Mk3GzZiffbAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAKAT+e2wVNEJld5CCtpqN13fvCJw7Yc2HZdeg648JqkrLOVyZltynMA2VG6NM4sDwQcH3Cb75nPcfND+rUrTSDiu0eQ3xfCh4pfsqvh8EEQ95yrGGm91McdGlNt24IUZNfGFzGZs/cCfuxoQUfpMCSSlVN/6SFpm8E5wHMYP9ALR+Aw8eVWMOOpDDzQWDZHo7lxkz4rMuaeWqOggAw+rMeOEblpWoU52Mxy02x+I8GUJdxjwL1atRI1yuUp7LkZ1O+NTtVmB0tdOvHpUlDGRPIbF2VfeurrIIs0y2Pz1TdCR5eSxjVHPleiabaKjWft8qUysGGptTr+FmGj9KG8h3L8=";
    private static final String N = "qtzjj3KuFldrDL3rpIsKoEjzxwHQEUmzQNHoCP8CYiA-29q-Tqt8s0ALg5ruK-UH5pBJxtixEuYG0jkNcHFLOfJz45W2Lc1kjYtOQbjMEDewrCG2pDDT-kBbjs_GaaOINQfV5e1Duv9BYpHSaQtrin-yeOX4Lu5U6qTRzuSo-bO29dB6Vr5Lj8vhEUm5vQE9R2vsM1ZyXwGV6LLwi-cOFRZfEhKgHhfoUzGuZqB4EI74LoyPytxTDguA3tSz2eccALjTdfor03NUSCd9qiOC11FGw0gVteoplkW7nH6dVMwfTfByGb6fTzYy2Yx_30kCO6yNwALQdDujJNxs2Yn32w";
    private static final String E = "AQAB";
    private static final String PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqtzjj3KuFldrDL3rpIsKoEjzxwHQEUmzQNHoCP8CYiA+29q+Tqt8s0ALg5ruK+UH5pBJxtixEuYG0jkNcHFLOfJz45W2Lc1kjYtOQbjMEDewrCG2pDDT+kBbjs/GaaOINQfV5e1Duv9BYpHSaQtrin+yeOX4Lu5U6qTRzuSo+bO29dB6Vr5Lj8vhEUm5vQE9R2vsM1ZyXwGV6LLwi+cOFRZfEhKgHhfoUzGuZqB4EI74LoyPytxTDguA3tSz2eccALjTdfor03NUSCd9qiOC11FGw0gVteoplkW7nH6dVMwfTfByGb6fTzYy2Yx/30kCO6yNwALQdDujJNxs2Yn32wIDAQAB";
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(KeyBuilder.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void createCertAndModulus() {
        
        KeyEntry certKey = KeyBuilder.newBuilder(KID)
            .withRsaAlgorithm(JWSAlgorithm.RS256)
            .withX509Certificate(CERT)
            .build();
        
        KeyEntry modulusKey = KeyBuilder.newBuilder(KID)
            .withRsaAlgorithm(JWSAlgorithm.RS256)
            .withModulusAndExponent(N, E)
            .build();
        
        assertEquals(KID, certKey.getKid());
        assertEquals(KID, modulusKey.getKid());
        
        assertEquals(new String(certKey.getVerificationKey().getEncoded()), new String(modulusKey.getVerificationKey().getEncoded()));
    }
    
    @Test
    public void createFromPubkey() {
        KeyEntry keyEntry = KeyBuilder.newBuilder(KID)
            .withRsaAlgorithm(JWSAlgorithm.RS256)
            .withPublicKey(PUB_KEY)
            .build();
        
        assertEquals(KID, keyEntry.getKid());
        
        String base64Key = new String(Base64.getEncoder().encode(keyEntry.getVerificationKey().getEncoded()));
        assertEquals(PUB_KEY, base64Key);
    }
    
}
