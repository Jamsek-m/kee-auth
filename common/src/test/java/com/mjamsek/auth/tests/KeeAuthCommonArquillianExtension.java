package com.mjamsek.auth.tests;

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class KeeAuthCommonArquillianExtension implements LoadableExtension {
    
    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        extensionBuilder.service(AuxiliaryArchiveAppender.class, KeeAuthCommonLibraryAppender.class);
    }
}
