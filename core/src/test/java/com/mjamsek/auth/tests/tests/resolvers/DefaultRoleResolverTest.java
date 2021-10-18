package com.mjamsek.auth.tests.tests.resolvers;

import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.exceptions.UnresolvableRolesException;
import com.mjamsek.auth.resolvers.DefaultRolesResolver;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class DefaultRoleResolverTest {
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(DefaultRolesResolver.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private static final Set<String> roles = Set.of("user", "dev");
    
    private Map<String, Object> claims;
    private Map<String, Object> malformedClaims;
    
    @Before
    public void init() {
        this.claims = new HashMap<>();
        this.claims.put("sub", "123");
        this.claims.put("iss", "self");
        this.claims.put("roles", new ArrayList<>(roles));
        this.malformedClaims = new HashMap<>();
    }
    
    private static RolesAllowed createAnnotation(Set<String> requiredRoles, String clientName) {
        return new RolesAllowed() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RolesAllowed.class;
            }
            
            @Override
            public String[] value() {
                String[] values = new String[requiredRoles.size()];
                return requiredRoles.toArray(values);
            }
            
            @Override
            public String clientName() {
                return clientName;
            }
        };
    }
    
    @Test
    public void roles() throws UnresolvableRolesException {
        DefaultRolesResolver resolver = new DefaultRolesResolver();
        
        RolesAllowed globalAnnotation = createAnnotation(Set.of(), "");
        Set<String> returnedRoles = resolver.resolveRoles(this.claims, globalAnnotation);
        assertEquals(roles, returnedRoles);
        
        RolesAllowed ignoredClientAnnotation = createAnnotation(Set.of(), "client-name");
        Set<String> returnedIgnoredRoles = resolver.resolveRoles(this.claims, ignoredClientAnnotation);
        assertEquals(roles, returnedIgnoredRoles);
        
        assertEquals(returnedRoles, returnedIgnoredRoles);
    }
    
    @Test(expected = UnresolvableRolesException.class)
    public void malformedClaims() throws UnresolvableRolesException {
        RolesAllowed annotation = createAnnotation(Set.of(), "");
        DefaultRolesResolver resolver = new DefaultRolesResolver();
        resolver.resolveRoles(this.malformedClaims, annotation);
    }
}
