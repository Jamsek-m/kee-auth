package com.mjamsek.auth.tests.tests.keys;

import com.mjamsek.auth.common.enums.VerificationAlgorithm;
import com.mjamsek.auth.models.keys.HmacJwtKey;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Arquillian.class)
public class HmacJwtKeyTest {
    
    private static final String KID = "abcde123";
    private static final String SECRET = "secretsecretsecretsecretsecretsecretkey";
    private static final String WEAK_SECRET = "weak";
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(HmacJwtKey.class)
            .addClass(Keys.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void create() throws InvalidKeySpecException {
        HmacJwtKey hmacJwtKey = new HmacJwtKey(KID, VerificationAlgorithm.HS512, SECRET);
        assertEquals(KID, hmacJwtKey.getKid());
        assertEquals(SECRET, new String(hmacJwtKey.getSecretKey().getEncoded()));
    }
    
    @Test
    public void missingKey() {
        assertThrows(InvalidKeySpecException.class, () -> new HmacJwtKey(KID, VerificationAlgorithm.HS512, null));
        assertThrows(InvalidKeySpecException.class, () -> new HmacJwtKey(KID, VerificationAlgorithm.HS512, ""));
        assertThrows(InvalidKeySpecException.class, () -> new HmacJwtKey(KID, VerificationAlgorithm.HS512, "  "));
    }
    
    @Test
    public void createWeak() {
        assertThrows(WeakKeyException.class, () -> new HmacJwtKey(KID, VerificationAlgorithm.HS512, WEAK_SECRET));
    }
    
}
