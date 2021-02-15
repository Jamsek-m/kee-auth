package com.mjamsek.auth.tests;

import org.jboss.arquillian.container.test.spi.client.deployment.CachedAuxilliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class KeeAuthCommonLibraryAppender extends CachedAuxilliaryArchiveAppender {
    
    @Override
    protected Archive<?> buildArchive() {
        return ShrinkWrap.create(JavaArchive.class, "kee-auth-common-0.0.0.jar")
            .addPackages(true, "com.mjamsek.auth")
            .addAsResource("META-INF/beans.xml");
    }
}
