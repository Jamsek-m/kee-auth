/*
 *  Copyright (c) 2019-2021 Miha Jamsek and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.mjamsek.auth;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.*;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;
import com.mjamsek.auth.config.KeeAuthInitializator;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
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
