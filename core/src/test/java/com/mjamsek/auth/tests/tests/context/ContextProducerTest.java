package com.mjamsek.auth.tests.tests.context;

import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.producers.ContextProducer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.security.Keys;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
            .addClass(Jwts.class)
            .addClass(Keys.class)
            .addClass(DefaultJwtBuilder.class)
            .addClass(ContextProducer.class)
            .addAsResource("context-config.yml", "config.yml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private String jwt;
    
    @Before
    public void init() {
        SecretKey secretKey = Keys.hmacShaKeyFor("secretsecretsecretsecretsecretsecretkey".getBytes(StandardCharsets.UTF_8));
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("email", TOKEN_EMAIL);
        
        this.jwt = Jwts.builder()
            .setSubject(TOKEN_SUBJECT)
            .setIssuer(TOKEN_ISS)
            .setHeaderParam("kid", TOKEN_KID)
            .addClaims(claims)
            .signWith(secretKey)
            .compact();
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
        
        Claims claims = context.getTokenPayload();
        assertEquals(TOKEN_SUBJECT, claims.getSubject());
        assertEquals(TOKEN_ISS, claims.getIssuer());
        assertEquals(roles, claims.get("roles"));
    }
    
}
