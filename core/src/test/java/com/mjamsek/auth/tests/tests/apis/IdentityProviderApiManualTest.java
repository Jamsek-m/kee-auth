package com.mjamsek.auth.tests.tests.apis;

import com.mjamsek.auth.apis.IdentityProviderApi;
import com.mjamsek.auth.config.KeeAuthConfig;
import com.mjamsek.auth.models.WellKnownConfig;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class IdentityProviderApiManualTest {
    
    @Deployment
    public static JavaArchive createDeployment2() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(IdentityProviderApi.class)
            .addClass(KeeAuthConfig.class)
            .addAsResource("webapp/well-known-2.json")
            .addAsResource("webapp/jwks.json")
            .addAsResource("local-idp-config-2.yml", "config.yml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "webapp/WEB-INF/web.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private static final String TOKEN_URL = "https://test.idp.com/token";
    private static final String JWKS_URL = "https://test.idp.com/jwks";
    private static final String ISSUER = "https://test.idp.com";
    
    @Test
    public void wellKnownConfig() {
        WellKnownConfig wellKnownConfig = IdentityProviderApi.getWellKnownConfig();
        
        assertEquals(ISSUER, wellKnownConfig.getIssuer());
        assertEquals(TOKEN_URL, wellKnownConfig.getTokenEndpoint());
        assertEquals(JWKS_URL, wellKnownConfig.getJwksUri());
    }
    
    private static final String JWKS_KID = "1234";
    private static final String JWKS_N = "123";
    private static final String JWKS_E = "13";
    private static final String JWKS_ALG = "RS256";
    
    @Test
    public void jwks() throws ParseException {
        WellKnownConfig wellKnownConfig = IdentityProviderApi.getWellKnownConfig();
        KeeAuthConfig.setWellKnownConfig(wellKnownConfig);
        
        JWKSet jsonWebKeySet = IdentityProviderApi.getJWKS();
        
        assertNotNull(jsonWebKeySet.getKeys());
        assertEquals(1, jsonWebKeySet.getKeys().size());
    
        JWK key = jsonWebKeySet.getKeys().get(0);
        assertEquals(JWKS_KID, key.getKeyID());
        assertEquals(JWKS_ALG, key.getAlgorithm().getName());
        
        assertTrue(key instanceof RSAKey);
        RSAKey rsaKey = (RSAKey) key;
        assertEquals(JWKS_E, rsaKey.getPublicExponent().toString());
        assertEquals(JWKS_N, rsaKey.getModulus().toString());
    }
    
}
