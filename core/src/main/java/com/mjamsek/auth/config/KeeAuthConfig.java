package com.mjamsek.auth.config;

import com.mjamsek.auth.models.WellKnownConfig;
import com.mjamsek.auth.models.keys.JwtSigningKey;

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
    private static final ConcurrentHashMap<String, JwtSigningKey> signingKeysMap = new ConcurrentHashMap<>();
    
    public synchronized static Optional<String> getTokenEndpoint() {
        WellKnownConfig wellKnownConfig = wellKnownConfigAtomic.get();
        if (wellKnownConfig == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(wellKnownConfig.getTokenEndpoint());
    }
    
    public synchronized static Optional<String> getJwksEndpoint() {
        WellKnownConfig wellKnownConfig = wellKnownConfigAtomic.get();
        if (wellKnownConfig == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(wellKnownConfig.getJwksUri());
    }
    
    public static void setWellKnownConfig(WellKnownConfig config) {
        wellKnownConfigAtomic.set(config);
    }
    
    static void addSigningKey(JwtSigningKey key) {
        signingKeysMap.put(key.getKid(), key);
    }
    
    public synchronized static Map<String, JwtSigningKey> getSigningKeys() {
        return new HashMap<>(signingKeysMap);
    }
    
}
