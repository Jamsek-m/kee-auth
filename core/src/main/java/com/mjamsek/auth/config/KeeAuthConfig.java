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

import com.mjamsek.auth.keys.KeyEntry;
import com.mjamsek.auth.models.WellKnownConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class KeeAuthConfig {
    
    private static final AtomicReference<WellKnownConfig> wellKnownConfigAtomic = new AtomicReference<>(null);
    
    private static final ConcurrentHashMap<String, KeyEntry> jsonWebKeySetCache = new ConcurrentHashMap<>();
    
    public synchronized static Optional<String> getTokenEndpoint() {
        WellKnownConfig wellKnownConfig = wellKnownConfigAtomic.get();
        if (wellKnownConfig == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(wellKnownConfig.getTokenEndpoint());
    }
    
    public synchronized static Optional<String> getIssuer() {
        WellKnownConfig wellKnownConfig = wellKnownConfigAtomic.get();
        if (wellKnownConfig == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(wellKnownConfig.getIssuer());
    }
    
    public synchronized static Optional<String> getJwksEndpoint() {
        WellKnownConfig wellKnownConfig = wellKnownConfigAtomic.get();
        if (wellKnownConfig == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(wellKnownConfig.getJwksUri());
    }
    
    public synchronized static Map<String, KeyEntry> getVerificationKeys() {
        return new HashMap<>(jsonWebKeySetCache);
    }
    
    public static void setWellKnownConfig(WellKnownConfig config) {
        wellKnownConfigAtomic.set(config);
    }
    
    public static void addVerificationKey(KeyEntry jsonWebKey) {
        jsonWebKeySetCache.put(jsonWebKey.getKid(), jsonWebKey);
    }
    
}
