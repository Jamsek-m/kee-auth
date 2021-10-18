package com.mjamsek.auth.tests.tests.context;

import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.producers.ContextProducer;
import com.mjamsek.auth.utils.DateUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class ContextProducerTest {
    
    private static final String TOKEN_SUBJECT = "123456";
    private static final String TOKEN_KID = "ac451sd4";
    private static final String TOKEN_ISS = "https://localhost:8090/auth";
    private static final String TOKEN_EMAIL = "john.smith@mail.com";
    private static final List<String> roles = List.of("user", "dev");
    
    @Deployment
    public static JavaArchive createDeployment1() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(JWTClaimsSet.class)
            .addClass(JWSHeader.class)
            .addClass(SignedJWT.class)
            .addClass(ContextProducer.class)
            .addAsResource("context-config.yml", "config.yml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private String jwt;
    
    @Before
    public void init() throws JOSEException {
        SecretKey secretKey = new SecretKeySpec("secretsecretsecretsecretsecretsecretkey".getBytes(StandardCharsets.UTF_8), JWSAlgorithm.HS256.getName());
        JWSSigner signer = new MACSigner(secretKey);
        
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(TOKEN_SUBJECT)
            .issuer(TOKEN_ISS)
            .expirationTime(DateUtil.addToDate(new Date(), 300))
            .claim("roles", roles)
            .claim("email", TOKEN_EMAIL)
            .build();
        
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
            .keyID(TOKEN_KID)
            .type(JOSEObjectType.JWT)
            .build();
        
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(signer);
        
        this.jwt = signedJWT.serialize();
    }
    
    @Test
    public void emptyContext() {
        AuthContext context = ContextProducer.produceEmptyContext();
        assertFalse(context.isAuthenticated());
        assertNull(context.getEmail());
        assertNull(context.getId());
        assertNull(context.getRawToken());
        assertNull(context.getScope());
        assertNull(context.getUsername());
        assertNull(context.getTokenPayload());
    }
    
    @Test
    public void actualContext() {
        AuthContext context = ContextProducer.produceContext(this.jwt);
        assertTrue(context.isAuthenticated());
        assertEquals(TOKEN_SUBJECT, context.getId());
        assertEquals(TOKEN_EMAIL, context.getEmail());
    
        Map<String, Object> claims = context.getTokenPayload();
        assertEquals(TOKEN_SUBJECT, claims.get("sub"));
        assertEquals(TOKEN_ISS, claims.get("iss"));
        assertEquals(roles, claims.get("roles"));
    }
    
}
