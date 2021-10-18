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

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class HmacJwtKeyTest {
    
    private static final String KID = "abcde123";
    private static final String SECRET = "secretsecretsecretsecretsecretsecretkey";
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(KeyBuilder.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void create() {
        KeyEntry keyEntry = KeyBuilder.newBuilder(KID)
            .withHmacAlgorithm(JWSAlgorithm.HS256)
            .withSecret(SECRET)
            .build();
        
        assertEquals(KID, keyEntry.getKid());
        assertEquals(SECRET, new String(keyEntry.getVerificationKey().getEncoded()));
    }
    
}
