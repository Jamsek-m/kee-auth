package com.mjamsek.auth.tests.tests.utils;

import com.mjamsek.auth.utils.TokenUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class TokenUtilTest {
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(TokenUtil.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private static final String untrimmedHeader = "Bearer 123456";
    private static final String trimmedHeader = "123456";
    
    @Test
    public void trimHeader() {
        assertEquals(trimmedHeader, TokenUtil.trimAuthorizationHeader(untrimmedHeader));
        assertEquals(trimmedHeader, TokenUtil.trimAuthorizationHeader(trimmedHeader));
        assertEquals("", TokenUtil.trimAuthorizationHeader(""));
        assertNull(TokenUtil.trimAuthorizationHeader(null));
    }
    
}
