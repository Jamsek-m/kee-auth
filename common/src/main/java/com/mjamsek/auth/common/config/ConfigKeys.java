package com.mjamsek.auth.common.config;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class ConfigKeys {
    
    // Clients
    public static final String CLIENTS = "kee-auth.clients";
    
    // OIDC
    public static final String AUTO_CONFIGURE = "kee-auth.oidc.auto-configure";
    public static final String WELL_KNOWN_URL = "kee-auth.oidc.well-known-url";
    public static final String USE_JWKS = "kee-auth.oidc.use-jwks";
    public static final String JWKS_URL = "kee-auth.oidc.jwks-url";
    public static final String CLIENT_CREDENTIALS_ID = "kee-auth.oidc.client-credentials.client-id";
    public static final String CLIENT_CREDENTIALS_SECRET = "kee-auth.oidc.client-credentials.client-secret";
    public static final String TOKEN_URL = "kee-auth.oidc.client-credentials.token-url";
    
    // JWT
    public static final String JWT_TIME_LEEWAY = "kee-auth.jwt.time-leeway";
    public static final String JWT_KEYS = "kee-auth.jwt.keys";
    // Mappings
    public static final String CLAIM_MAPPING_ID = "kee-auth.jwt.claims.id";
    public static final String CLAIM_MAPPING_EMAIL = "kee-auth.jwt.claims.email";
    public static final String CLAIM_MAPPING_USERNAME = "kee-auth.jwt.claims.username";
    public static final String CLAIM_MAPPING_SCOPE = "kee-auth.jwt.claims.scope";
    // Keys
    public static final String KEY_KID_POSTFIX = "kid";
    public static final String KEY_ALG_POSTFIX = "alg";
    public static final String KEY_N_POSTFIX = "n";
    public static final String KEY_E_POSTFIX = "e";
    public static final String KEY_X5C_POSTFIX = "x5c";
    public static final String KEY_SECRET_POSTFIX = "secret";
    public static final String KEY_CRV_POSTFIX = "crv";
    public static final String KEY_X_POSTFIX = "x";
    public static final String KEY_Y_POSTFIX = "y";
    
    // Resolvers
    public static final String CLAIM_MAPPING_ROLES = "kee-auth.role-resolvers.default.roles-mapping";
    public static final String RESOLVER_ENABLED_PREFIX = "kee-auth.role-resolvers";
    public static final String RESOLVER_ENABLED_POSTFIX = "enabled";
    
}
