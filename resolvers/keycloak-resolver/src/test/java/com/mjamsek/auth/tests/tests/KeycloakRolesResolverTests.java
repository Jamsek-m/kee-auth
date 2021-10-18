package com.mjamsek.auth.tests.tests;

import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.exceptions.UnresolvableRolesException;
import com.mjamsek.auth.resolvers.keycloak.KeycloakRolesResolver;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class KeycloakRolesResolverTests {
    
    private static final String REALM_ACCESS_KEY = "realm_access";
    private static final String CLIENT_ACCESS_KEY = "resource_access";
    private static final String ROLES_KEY = "roles";
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(KeycloakRolesResolver.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private static final String CLIENT_1_NAME = "client-1";
    private static final String CLIENT_2_NAME = "client-2";
    private static final Set<String> realmRoles = Set.of("user", "dev");
    private static final Set<String> client1Roles = Set.of("user");
    private static final Set<String> client2Roles = new HashSet<>();
    
    private Map<String, Object> claims;
    private Map<String, Object> malformedClaims;
    
    @Before
    public void init() {
        this.malformedClaims = new HashMap<>();
        this.claims = new HashMap<>();
        
        this.claims.put("sub", "123");
        this.claims.put("iss", "self");
        this.claims.put(REALM_ACCESS_KEY, createRolesNode(realmRoles));
        Map<String, Object> clientAccesses = new HashMap<>();
        clientAccesses.put(CLIENT_1_NAME, createRolesNode(client1Roles));
        clientAccesses.put(CLIENT_2_NAME, createRolesNode(client2Roles));
        this.claims.put(CLIENT_ACCESS_KEY, clientAccesses);
    }
    
    private static Map<String, Object> createRolesNode(Set<String> roles) {
        Map<String, Object> map = new HashMap<>();
        map.put(ROLES_KEY, new ArrayList<>(roles));
        return map;
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
    public void realmRoles() throws UnresolvableRolesException {
        RolesAllowed realmAnnotation = createAnnotation(Set.of(), "");
        
        KeycloakRolesResolver resolver = new KeycloakRolesResolver();
        Set<String> returnedRoles = resolver.resolveRoles(this.claims, realmAnnotation);
        
        assertEquals(realmRoles, returnedRoles);
    }
    
    @Test
    public void clientRoles() throws UnresolvableRolesException {
        KeycloakRolesResolver resolver = new KeycloakRolesResolver();
        
        RolesAllowed clientAnnotation = createAnnotation(Set.of(), CLIENT_1_NAME);
        Set<String> returnedRoles = resolver.resolveRoles(this.claims, clientAnnotation);
        assertEquals(client1Roles, returnedRoles);
        
        clientAnnotation = createAnnotation(Set.of(), CLIENT_2_NAME);
        returnedRoles = resolver.resolveRoles(this.claims, clientAnnotation);
        assertEquals(client2Roles, returnedRoles);
    }
    
    @Test(expected = UnresolvableRolesException.class)
    public void malformedClaims() throws UnresolvableRolesException {
        RolesAllowed clientAnnotation = createAnnotation(Set.of(), CLIENT_2_NAME);
        KeycloakRolesResolver resolver = new KeycloakRolesResolver();
        resolver.resolveRoles(this.malformedClaims, clientAnnotation);
    }
    
}
