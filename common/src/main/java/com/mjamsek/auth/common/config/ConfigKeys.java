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
    
    public static final String CLIENTS = "kee-auth.clients";
    
    public static class Oidc {
        public static final String ISSUER = "kee-auth.oidc.issuer";
        public static final String AUTO_CONFIGURE = "kee-auth.oidc.auto-configure";
        public static final String WELL_KNOWN_URL = "kee-auth.oidc.well-known-url";
        public static final String USE_JWKS_URI = "kee-auth.oidc.use-jwks-uri";
        public static final String JWKS_URL = "kee-auth.oidc.jwks-url";
        
        public static class ClientCredentials {
            public static final String CLIENT_ID = "kee-auth.oidc.client-credentials.client-id";
            public static final String CLIENT_SECRET = "kee-auth.oidc.client-credentials.client-secret";
            public static final String TOKEN_URL = "kee-auth.oidc.client-credentials.token-url";
        }
        
        public static class Credentials {
            public static final String LOCATION = "kee-auth.oidc.credentials.location";
            public static final String COOKIE_NAME = "kee-auth.oidc.credentials.cookie-name";
            public static final String HEADER_NAME = "kee-auth.oidc.credentials.header-name";
        }
    }
    
    public static class Jwt {
        public static final String TIME_LEEWAY = "kee-auth.jwt.time-leeway";
        public static final String KEYS = "kee-auth.jwt.keys";
        
        public static class Claims {
            public static final String ID_MAPPING = "kee-auth.jwt.claims.id";
            public static final String EMAIL_MAPPING = "kee-auth.jwt.claims.email";
            public static final String USERNAME_MAPPING = "kee-auth.jwt.claims.username";
            public static final String SCOPE_MAPPING = "kee-auth.jwt.claims.scope";
        }
        
        public static class Keys {
            public static final String KID_POSTFIX = "kid";
            public static final String ALG_POSTFIX = "alg";
            public static final String N_POSTFIX = "n";
            public static final String E_POSTFIX = "e";
            public static final String X5C_POSTFIX = "x5c";
            public static final String PUB_KEY_POSTFIX = "public-key";
            public static final String SECRET_POSTFIX = "secret";
            public static final String CRV_POSTFIX = "crv";
            public static final String X_POSTFIX = "x";
            public static final String Y_POSTFIX = "y";
        }
        
    }
    
    public static class RoleResolvers {
        public static final String DEFAULT_ROLES_MAPPING = "kee-auth.role-resolvers.default.roles-mapping";
        
        public static final String RESOLVER_ENABLED_PREFIX = "kee-auth.role-resolvers";
        public static final String RESOLVER_ENABLED_POSTFIX = "enabled";
    }
}
