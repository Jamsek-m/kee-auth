package com.mjamsek.auth.tests.tests.apis;

import com.mjamsek.auth.apis.IdentityProviderApi;
import com.mjamsek.auth.config.KeeAuthConfig;
import com.mjamsek.auth.models.JsonWebKey;
import com.mjamsek.auth.models.JsonWebKeySet;
import com.mjamsek.auth.models.WellKnownConfig;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class IdentityProviderApiTest {
    
    @Deployment
    public static JavaArchive createDeployment1() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(IdentityProviderApi.class)
            .addClass(KeeAuthConfig.class)
            .addAsResource("webapp/well-known.json")
            .addAsResource("webapp/jwks.json")
            .addAsResource("local-idp-config.yml", "config.yml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "webapp/WEB-INF/web.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Before
    public void init() {
    
    }
    
    private static final String TOKEN_URL = "https://test.idp.com/token";
    private static final String JWKS_URL = "http://localhost:44444/jwks.json";
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
    public void jwks() {
        WellKnownConfig wellKnownConfig = IdentityProviderApi.getWellKnownConfig();
        KeeAuthConfig.setWellKnownConfig(wellKnownConfig);
        
        JsonWebKeySet jsonWebKeySet = IdentityProviderApi.getJsonWebKeySet();
        
        assertNotNull(jsonWebKeySet.getKeys());
        assertEquals(1, jsonWebKeySet.getKeys().size());
    
        JsonWebKey key = jsonWebKeySet.getKeys().get(0);
        assertEquals(JWKS_KID, key.getKid());
        assertEquals(JWKS_ALG, key.getAlg());
        assertEquals(JWKS_E, key.getE());
        assertEquals(JWKS_N, key.getN());
        assertNull(key.getCrv());
    }
    
}
