package com.mjamsek.auth.tests.tests;

import com.mjamsek.auth.common.mappings.ClientNameMapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class ClientNameMapperTest {
    
    private static final String CLIENT_NAME_1 = "internal-client-1";
    private static final String CLIENT_NAME_2 = "internal-client-2";
    private static final String CLIENT_NAME_3 = "internal-client-3";
    private static final String CLIENT_NAME_4 = "internal-client-4";
    private static final String CLIENT_ID_1 = "external-client-1";
    private static final String CLIENT_ID_2 = "external-client-2";
    private static final String CLIENT_ID_4 = "external-client-4";
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(ClientNameMapper.class)
            .addAsResource("client-mapping-config.yml", "config.yml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void clientMappingsFromConfig() {
        Map<String, String> mappings = ClientNameMapper.getClientRoleMappings();
        assertEquals(CLIENT_ID_1, mappings.get(CLIENT_NAME_1));
        assertEquals(CLIENT_ID_2, mappings.get(CLIENT_NAME_2));
        assertEquals(CLIENT_ID_4, mappings.get(CLIENT_NAME_4));
        assertNull(mappings.get(CLIENT_NAME_3));
    }
    
}
