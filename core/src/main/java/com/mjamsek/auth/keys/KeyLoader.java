package com.mjamsek.auth.keys;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.apis.IdentityProviderApi;
import com.mjamsek.auth.common.config.ConfigDefaults;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.common.exceptions.HttpCallException;
import com.mjamsek.auth.common.exceptions.MissingConfigException;
import com.mjamsek.auth.config.KeeAuthConfig;
import com.mjamsek.auth.mappers.JsonWebKeyMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;

import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class KeyLoader {
    
    private static KeyLoader INSTANCE = null;
    
    public static KeyLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KeyLoader();
        }
        return INSTANCE;
    }
    
    private KeyLoader() {
    
    }
    
    private static final Logger LOG = Logger.getLogger(KeyLoader.class.getName());
    
    public Optional<KeyEntry> loadKey(String kid) {
        final Map<String, KeyEntry> localKeys = getKeysFromConfiguration();
        if (localKeys.containsKey(kid)) {
            return Optional.of(localKeys.get(kid));
        }
        
        if (ConfigDefaults.useJwks()) {
            Map<String, KeyEntry> remoteKeys = KeeAuthConfig.getVerificationKeys();
            if (remoteKeys.containsKey(kid)) {
                return Optional.of(remoteKeys.get(kid));
            }
            
            // If key is not present, try to refetch JWKS
            remoteKeys = fetchKeysFromJwks();
            if (remoteKeys.containsKey(kid)) {
                return Optional.of(remoteKeys.get(kid));
            }
        }
        
        return Optional.empty();
    }
    
    public Map<String, KeyEntry> fetchKeysFromJwks() {
        try {
            LOG.fine("Fetching public keys from JWKS endpoint ...");
            JWKSet jwkSet = IdentityProviderApi.getJWKS();
            List<KeyEntry> keyEntries = jwkSet.getKeys()
                .stream()
                .map(JsonWebKeyMapper::toKeyEntry)
                .collect(Collectors.toUnmodifiableList());
            LOG.fine("Fetched public keys!");
            return keyEntries.stream()
                .peek(KeeAuthConfig::addVerificationKey) // Stores keys into config
                .collect(Collectors.toMap(KeyEntry::getKid, k -> k));
        } catch (HttpCallException e) {
            LOG.severe(e.getMessage());
            LOG.severe("Unable to fetch keys from jwks endpoint! Falling back to using locally provided keys.");
            return null;
        } catch (ParseException e) {
            LOG.severe(e.getMessage());
            LOG.severe("Unable to parse response payload! Received JWKS is unreadable! Falling back to using locally provided keys.");
            return null;
        } catch (MissingConfigException e) {
            LOG.warning(e.getMessage());
            LOG.warning("Falling back to using only locally provided keys.");
            return null;
        }
    }
    
    private Map<String, KeyEntry> getKeysFromConfiguration() {
        Map<String, KeyEntry> keyMap = new HashMap<>();
        int listSize = ConfigurationUtil.getInstance().getListSize(ConfigKeys.Jwt.KEYS).orElse(0);
        for (int i = 0; i < listSize; i++) {
            try {
                KeyEntry key = readKeyFromConfiguration(ConfigKeys.Jwt.KEYS + "[" + i + "].");
                keyMap.put(key.getKid(), key);
            } catch (InvalidKeySpecException e) {
                LOG.warning(e.getMessage());
                LOG.warning("Malformed key configuration! Key will be ignored!");
            }
        }
        return keyMap;
    }
    
    private KeyEntry readKeyFromConfiguration(String configPrefix) throws InvalidKeySpecException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        String kid = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.KID_POSTFIX)
            .orElseThrow(() -> new InvalidKeySpecException(""));
        String alg = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.ALG_POSTFIX)
            .orElseThrow(() -> new InvalidKeySpecException(""));
        
        JWSAlgorithm algorithm = JWSAlgorithm.parse(alg);
        if (algorithm == null) {
            throw new InvalidKeySpecException("");
        }
        
        if (JWSAlgorithm.Family.RSA.contains(algorithm)) {
            return createRSASigningKey(kid, algorithm, configPrefix);
        } else if (JWSAlgorithm.Family.EC.contains(algorithm)) {
            return createECSigningKey(kid, algorithm, configPrefix);
        } else if (JWSAlgorithm.Family.HMAC_SHA.contains(algorithm)) {
            return createHmacSigningKey(kid, algorithm, configPrefix);
        } else {
            throw new RuntimeException("");
        }
    }
    
    private KeyEntry createHmacSigningKey(String kid, JWSAlgorithm algorithm, String configPrefix) throws InvalidKeySpecException {
        Optional<String> secret = ConfigurationUtil.getInstance().get(configPrefix + ConfigKeys.Jwt.Keys.SECRET_POSTFIX);
        if (secret.isPresent()) {
            return KeyBuilder.newBuilder(kid)
                .withHmacAlgorithm(algorithm)
                .withSecret(secret.get())
                .build();
        } else {
            throw new InvalidKeySpecException("Missing required parameters for ");
        }
    }
    
    private KeyEntry createRSASigningKey(String kid, JWSAlgorithm algorithm, String configPrefix) throws InvalidKeySpecException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        Optional<String> n = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.N_POSTFIX);
        Optional<String> e = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.E_POSTFIX);
        Optional<String> x5c = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.X5C_POSTFIX);
        Optional<String> publicKey = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.PUB_KEY_POSTFIX);
        
        if (n.isPresent() && e.isPresent()) {
            return KeyBuilder.newBuilder(kid)
                .withRsaAlgorithm(algorithm)
                .withModulusAndExponent(n.get(), e.get())
                .build();
        } else if (publicKey.isPresent()) {
            return KeyBuilder.newBuilder(kid)
                .withRsaAlgorithm(algorithm)
                .withPublicKey(publicKey.get())
                .build();
        } else if (x5c.isPresent()) {
            return KeyBuilder.newBuilder(kid)
                .withRsaAlgorithm(algorithm)
                .withX509Certificate(x5c.get())
                .build();
        } else {
            throw new InvalidKeySpecException("Missing required parameters for ");
        }
    }
    
    private KeyEntry createECSigningKey(String kid, JWSAlgorithm algorithm, String configPrefix) throws InvalidKeySpecException {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        Optional<String> x = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.X_POSTFIX);
        Optional<String> y = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.Y_POSTFIX);
        Optional<String> crv = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.CRV_POSTFIX);
        Optional<String> publicKey = configUtil.get(configPrefix + ConfigKeys.Jwt.Keys.PUB_KEY_POSTFIX);
        
        if (x.isPresent() && y.isPresent() && crv.isPresent()) {
            return KeyBuilder.newBuilder(kid)
                .withECAlgorithm(algorithm)
                .withCurveParameters(crv.get(), x.get(), y.get())
                .build();
        } else if (publicKey.isPresent()) {
            return KeyBuilder.newBuilder(kid)
                .withECAlgorithm(algorithm)
                .withPublicKey(publicKey.get())
                .build();
        } else {
            throw new InvalidKeySpecException("Missing required parameters for ");
        }
    }
}
