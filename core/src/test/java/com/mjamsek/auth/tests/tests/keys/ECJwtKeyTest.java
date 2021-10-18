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
public class ECJwtKeyTest {
    
    private static final String KID = "abcde123";
    private static final String X = "JNfddWwxilwKKtjwu3XZYrEQ3p9KXmtl_FAz-W62xdc";
    private static final String Y = "K5W77X7R8cRZFO640HUhAJY9_T1IKBoTo6LHrZWtLsA";
    private static final String CRV = "P-256";
    private static final String PUB_KEY = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEJNfddWwxilwKKtjwu3XZYrEQ3p9KXmtl/FAz+W62xdcrlbvtftHxxFkU7rjQdSEAlj39PUgoGhOjosetla0uwA==";
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(KeyBuilder.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void createCertAndModulus() {
        
        KeyEntry crvKey = KeyBuilder.newBuilder(KID)
            .withECAlgorithm(JWSAlgorithm.ES256)
            .withCurveParameters(CRV, X, Y)
            .build();
        
        assertEquals(KID, crvKey.getKid());
    
        String base64Key = new String(Base64.getEncoder().encode(crvKey.getVerificationKey().getEncoded()));
        assertEquals(PUB_KEY, base64Key);
    }
    
    @Test
    public void createFromPubkey() {
        KeyEntry keyEntry = KeyBuilder.newBuilder(KID)
            .withECAlgorithm(JWSAlgorithm.ES256)
            .withPublicKey(PUB_KEY)
            .build();
        
        assertEquals(KID, keyEntry.getKid());
        
        String base64Key = new String(Base64.getEncoder().encode(keyEntry.getVerificationKey().getEncoded()));
        assertEquals(PUB_KEY, base64Key);
    }
    
}
