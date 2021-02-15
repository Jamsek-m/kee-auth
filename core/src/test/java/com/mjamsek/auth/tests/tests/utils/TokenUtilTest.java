package com.mjamsek.auth.tests.tests.utils;

import com.mjamsek.auth.utils.TokenUtil;
import io.jsonwebtoken.*;
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
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class TokenUtilTest {
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(JacksonSerializer.class)
            .addClass(TokenUtil.class)
            .addClass(Jwts.class)
            .addClass(Keys.class)
            .addClass(TestJwtSigningKeyResolver.class)
            .addClass(DefaultJwtBuilder.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private static final String TOKEN_SUBJECT = "123456";
    private static final String TOKEN_KID = "ac451sd4";
    private static final String TOKEN_ISS = "https://localhost:8090/auth";
    private static final String untrimmedHeader = "Bearer 123456";
    private static final String trimmedHeader = "123456";
    private static final List<String> roles = List.of("user", "dev");
    
    @Test
    public void trimHeader() {
        assertEquals(trimmedHeader, TokenUtil.trimAuthorizationHeader(untrimmedHeader));
        assertEquals(trimmedHeader, TokenUtil.trimAuthorizationHeader(trimmedHeader));
        assertEquals("", TokenUtil.trimAuthorizationHeader(""));
        assertNull(TokenUtil.trimAuthorizationHeader(null));
    }
    
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
    
    private SecretKey secretKey;
    
    private String jwt;
    
    @Test
    public void parseJwt() {
        SigningKeyResolver keyResolver = new TestJwtSigningKeyResolver(secretKey);
        Jws<Claims> token = TokenUtil.parseJwt(jwt, keyResolver);
        
        assertEquals(TOKEN_KID, token.getHeader().getKeyId());
        assertEquals(TOKEN_ISS, token.getBody().getIssuer());
        assertEquals(TOKEN_SUBJECT, token.getBody().getSubject());
        assertEquals(roles, token.getBody().get("roles"));
    }
    
}
