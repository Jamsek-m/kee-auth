package com.mjamsek.auth.tests;

import org.jboss.arquillian.container.test.spi.client.deployment.CachedAuxilliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class KeycloakResolverLibraryAppender extends CachedAuxilliaryArchiveAppender {
    
    @Override
    protected Archive<?> buildArchive() {
        return ShrinkWrap.create(JavaArchive.class, "kee-auth-keycloak-resolver-0.0.0.jar")
            .addPackages(true, "com.mjamsek.auth")
            .addAsResource("META-INF/beans.xml");
    }
}
