package com.mjamsek.auth.common.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.ws.rs.core.HttpHeaders;

public class ConfigDefaults {
    
    private static final boolean DEFAULT_AUTOCONFIGURE_VALUE = false;
    private static final String DEFAULT_CREDENTIALS_COOKIE_NAME = "authorization";
    private static final String DEFAULT_CREDENTIALS_HEADERS_NAME = HttpHeaders.AUTHORIZATION;
    
    /**
     * Checks if autoconfiguration is enabled.
     * @return Returns <code>true</code> if autoconfiguration is specifically enabled, otherwise <code>false</code>.
     */
    public static boolean autoconfigurationEnabled() {
        return ConfigurationUtil.getInstance()
            .getBoolean(ConfigKeys.Oidc.AUTO_CONFIGURE)
            .orElse(DEFAULT_AUTOCONFIGURE_VALUE);
    }
    
    /**
     * Checks if jwks autoconfiguration is enabled.
     * @return Returns <code>true</code> if jwks autoconfiguration is specifically enabled.
     * If not, it checks if autoconfiguration is enabled, using method {@link #autoconfigurationEnabled()}.
     */
    public static boolean useJwks() {
        return ConfigurationUtil.getInstance()
            .getBoolean(ConfigKeys.Oidc.USE_JWKS_URI)
            .orElseGet(ConfigDefaults::autoconfigurationEnabled);
    }
    
    /**
     * Checks if override is provided for cookie credentials name.
     * @return Returns name of credentials cookie.
     */
    public static String getCredentialsCookieName() {
        return ConfigurationUtil.getInstance()
            .get(ConfigKeys.Oidc.Credentials.COOKIE_NAME)
            .orElse(DEFAULT_CREDENTIALS_COOKIE_NAME);
    }
    
    /**
     * Checks if override is provided for header credentials name.
     * @return Returns name of credentials header.
     */
    public static String getCredentialsHeaderName() {
        return ConfigurationUtil.getInstance()
            .get(ConfigKeys.Oidc.Credentials.HEADER_NAME)
            .orElse(DEFAULT_CREDENTIALS_HEADERS_NAME);
    }
}
