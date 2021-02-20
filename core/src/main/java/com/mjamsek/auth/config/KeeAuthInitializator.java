package com.mjamsek.auth.config;


import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.apis.IdentityProviderApi;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.common.exceptions.HttpCallException;
import com.mjamsek.auth.common.exceptions.MissingConfigException;
import com.mjamsek.auth.keys.KeyLoader;
import com.mjamsek.auth.models.JsonWebKeySet;
import com.mjamsek.auth.models.WellKnownConfig;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class KeeAuthInitializator {
    
    private static final Logger LOG = Logger.getLogger(KeeAuthInitializator.class.getName());
    
    public static void initializeKeeAuth() {
        boolean autoConfigure = ConfigurationUtil.getInstance().getBoolean(ConfigKeys.AUTO_CONFIGURE).orElse(false);
        if (autoConfigure) {
            loadConfigurationAsync();
        }
    }
    
    private static void loadConfigurationAsync() {
        LOG.info("Autoconfiguring KeeAuth, using .well-known endpoint...");
        CompletableFuture.runAsync(() -> {
            try {
                // Retrieve & store well known config
                loadWellKnownConfig();
                LOG.fine("Retrieved .well-known configuration!");
            } catch (HttpCallException e) {
                LOG.severe("Unable to retrieve configuration from .well-known endpoint! " + e.getMessage());
            } catch (MissingConfigException e) {
                LOG.warning(e.getMessage());
                LOG.warning("Falling back to using local configuration.");
            }
            try {
                // Retrieve JWKS
                boolean useJwks = ConfigurationUtil.getInstance().getBoolean(ConfigKeys.USE_JWKS).orElse(true);
                if (useJwks) {
                    loadJwks();
                    LOG.fine("Retrieved JWKS!");
                }
            } catch (HttpCallException e) {
                LOG.severe("Unable to retrieve keys from jwks endpoint! " + e.getMessage());
                return;
            } catch (MissingConfigException e) {
                LOG.info(e.getMessage());
                LOG.info("Falling back to using only locally provided keys.");
                return;
            }
            
            LOG.info("KeeAuth autoconfiguration complete!");
        }).exceptionally(throwable -> {
            LOG.severe("Error occurred during autoconfiguration!");
            LOG.severe(throwable.getMessage());
            throwable.printStackTrace();
            return null;
        });
    }
    
    public static void loadWellKnownConfig() throws MissingConfigException, HttpCallException {
        WellKnownConfig wellKnownConfig = IdentityProviderApi.getWellKnownConfig();
        KeeAuthConfig.setWellKnownConfig(wellKnownConfig);
    }
    
    public static void loadJwks() throws MissingConfigException, HttpCallException {
        JsonWebKeySet jsonWebKeySet = IdentityProviderApi.getJsonWebKeySet();
        // Parse keys and store them
        KeyLoader.loadKeys(jsonWebKeySet.getKeys())
            .forEach(KeeAuthConfig::addSigningKey);
    }
    
}
