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
package com.mjamsek.auth.config;


import com.mjamsek.auth.apis.IdentityProviderApi;
import com.mjamsek.auth.common.config.ConfigDefaults;
import com.mjamsek.auth.common.exceptions.HttpCallException;
import com.mjamsek.auth.common.exceptions.MissingConfigException;
import com.mjamsek.auth.keys.KeyLoader;
import com.mjamsek.auth.models.WellKnownConfig;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class KeeAuthInitializator {
    
    private static final Logger LOG = Logger.getLogger(KeeAuthInitializator.class.getName());
    
    public static void initializeKeeAuth() {
        boolean autoConfigure = ConfigDefaults.autoconfigurationEnabled();
        if (autoConfigure) {
            loadConfigurationAsync();
            return;
        }
        
        loadJwks();
    }
    
    private static void loadConfigurationAsync() {
        CompletableFuture.runAsync(() -> {
            LOG.info("Autoconfiguring KeeAuth ...");
            loadWellKnownConfig();
            loadJwks();
            LOG.info("KeeAuth autoconfiguration complete!");
        }).exceptionally(throwable -> {
            LOG.severe("Error occurred during autoconfiguration!");
            LOG.severe(throwable.getMessage());
            throwable.printStackTrace();
            return null;
        });
    }
    
    /**
     * Fetches well-known configuration from well-known endpoint and stores it in configuration
     *
     * @throws MissingConfigException If no well-known endpoint was provided
     * @throws HttpCallException      If call to well-known endpoint fails for any reason
     */
    public static void loadWellKnownConfig() throws MissingConfigException, HttpCallException {
        try {
            LOG.fine("Fetching .well-known endpoint ...");
            WellKnownConfig wellKnownConfig = IdentityProviderApi.getWellKnownConfig();
            KeeAuthConfig.setWellKnownConfig(wellKnownConfig);
            LOG.fine("Fetched .well-known configuration!");
        } catch (HttpCallException e) {
            LOG.severe(e.getMessage());
            LOG.severe("Unable to fetch configuration from .well-known endpoint! Falling back to using local configuration.");
        } catch (MissingConfigException e) {
            LOG.warning(e.getMessage());
            LOG.warning("Falling back to using local configuration.");
        }
    }
    
    /**
     * Fetches JWKS from jwks endpoint and stores it in configuration
     *
     * @throws MissingConfigException If no jwks endpoint was provided
     * @throws HttpCallException      If call to jwks endpoint fails for any reason
     */
    public static void loadJwks() throws MissingConfigException, HttpCallException {
        if (ConfigDefaults.useJwks()) {
            KeyLoader.getInstance().fetchKeysFromJwks();
        }
    }
}
