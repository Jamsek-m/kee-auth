package com.mjamsek.auth.tests.tests.context;

import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.producers.ContextProducer;
import com.mjamsek.auth.tests.tests.utils.TestJwtSigningKeyResolver;
import com.mjamsek.auth.utils.TokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class ContextProducerTest {
    
    private static final String TOKEN_SUBJECT = "123456";
    private static final String TOKEN_KID = "ac451sd4";
    private static final String TOKEN_ISS = "https://localhost:8090/auth";
    private static final List<String> roles = List.of("user", "dev");
    
    @Deployment
    public static JavaArchive createDeployment1() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(JacksonSerializer.class)
            .addClass(TokenUtil.class)
            .addClass(Jwts.class)
            .addClass(Keys.class)
            .addClass(TestJwtSigningKeyResolver.class)
            .addClass(DefaultJwtBuilder.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private SecretKey secretKey;
    
    private String jwt;
    
    @Before
    public void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
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
    
}
