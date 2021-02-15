package com.mjamsek.auth;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.*;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;
import com.mjamsek.auth.config.KeeAuthInitializator;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

@EeExtensionDef(name = "KeeAuth", group = EeExtensionGroup.SECURITY)
@EeComponentDependencies({
    @EeComponentDependency(EeComponentType.SERVLET),
    @EeComponentDependency(EeComponentType.CDI),
    @EeComponentDependency(EeComponentType.JAX_RS)
})
@ApplicationScoped
public class KeeAuthExtension implements Extension {
    
    private static final Logger LOG = Logger.getLogger(KeeAuthExtension.class.getName());
    
    @Override
    public void load() {
        LOG.info("Initializing security implemented by KeeAuth.");
    }
    
    @Override
    public void init(KumuluzServerWrapper server, EeConfig eeConfig) {
        KeeAuthInitializator.initializeKeeAuth();
        LOG.info("Initialized security implemented by KeeAuth.");
    }
}
