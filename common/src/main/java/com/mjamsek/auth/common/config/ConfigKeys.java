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
