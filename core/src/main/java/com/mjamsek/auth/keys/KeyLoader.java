package com.mjamsek.auth.keys;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.common.enums.VerificationAlgorithm;
import com.mjamsek.auth.models.JsonWebKey;
import com.mjamsek.auth.models.keys.HmacJwtKey;
import com.mjamsek.auth.models.keys.JwtSigningKey;
import com.mjamsek.auth.models.keys.RsaJwtKey;
import io.jsonwebtoken.security.WeakKeyException;

import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class KeyLoader {
    
    private static final Logger LOG = Logger.getLogger(KeyLoader.class.getName());
    
    private static final Set<VerificationAlgorithm> RSA_SET = Set.of(VerificationAlgorithm.RS256, VerificationAlgorithm.RS384, VerificationAlgorithm.RS512);
    private static final Set<VerificationAlgorithm> HMAC_SET = Set.of(VerificationAlgorithm.HS256, VerificationAlgorithm.HS384, VerificationAlgorithm.HS512);
    private static final Set<VerificationAlgorithm> EC_SET = Set.of(VerificationAlgorithm.ES256, VerificationAlgorithm.ES384, VerificationAlgorithm.ES512);
    
    public static List<JwtSigningKey> loadKeys(List<JsonWebKey> keys) {
        List<JwtSigningKey> signingKeys = new ArrayList<>();
        for (JsonWebKey jsonWebKey : keys) {
            try {
                if (jsonWebKey.getKid() == null || jsonWebKey.getKid().isBlank()) {
                    throw new InvalidKeySpecException("Missing kid (key id)!");
                }
                if (jsonWebKey.getAlg() == null || jsonWebKey.getAlg().isBlank()) {
                    throw new InvalidKeySpecException("Missing alg (signing algorithm)!");
                }
                
                // Parse algorithm string to known algorithm
                VerificationAlgorithm algorithm = VerificationAlgorithm.valueOf(jsonWebKey.getAlg());
                
                // Handle RSA public keys
                if (algorithm.isPartOfSet(RSA_SET)) {
                    RsaJwtKey rsaKey;
                    if (jsonWebKey.getX5c() != null && jsonWebKey.getX5c().size() > 0) {
                        String x5c = jsonWebKey.getX5c().get(0);
                        rsaKey = new RsaJwtKey(jsonWebKey.getKid(), x5c);
                    } else {
                        rsaKey = new RsaJwtKey(jsonWebKey.getKid(), jsonWebKey.getN(), jsonWebKey.getE());
                    }
                    signingKeys.add(rsaKey);
                } else if (algorithm.isPartOfSet(HMAC_SET)) {
                    HmacJwtKey hmacKey = new HmacJwtKey(jsonWebKey.getKid(), algorithm, jsonWebKey.getSecret());
                    signingKeys.add(hmacKey);
                } else {
                    // EC is not yet supported, skip its creation.
                    String unknownAlgorithm = jsonWebKey.getAlg();
                    String unknownKid = jsonWebKey.getKid();
                    LOG.warning("Unsupported algorithm: " + unknownAlgorithm +
                        "! Cannot create key with id: " + unknownKid + ", skipping...");
                }
            } catch (IllegalArgumentException e) {
                LOG.warning("Unknown algorithm '" + jsonWebKey.getAlg() + "'! Skipping this key...");
            } catch (WeakKeyException e) {
                LOG.warning(e.getMessage());
                LOG.warning("Skipping this key...");
            } catch (InvalidKeySpecException e) {
                LOG.warning("Invalid key spec '" + jsonWebKey.getKid() + "'! " + e.getMessage() + " Skipping this key...");
                e.printStackTrace();
            }
        }
        return signingKeys;
    }
    
    public static List<JsonWebKey> readKeysFromConfiguration() {
        List<JsonWebKey> jsonWebKeys = new ArrayList<>();
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        
        int listSize = configUtil.getListSize(ConfigKeys.JWT_KEYS).orElse(0);
        for (int i = 0; i < listSize; i++) {
            Optional<String> kid = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_KID_POSTFIX);
            Optional<String> alg = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_ALG_POSTFIX);
            
            if (kid.isPresent() && alg.isPresent()) {
                JsonWebKey jsonWebKey = new JsonWebKey();
                jsonWebKey.setKid(kid.get());
                jsonWebKey.setAlg(alg.get());
                
                Optional<String> n = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_N_POSTFIX);
                Optional<String> e = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_E_POSTFIX);
                Optional<String> secret = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_SECRET_POSTFIX);
                Optional<String> crv = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_CRV_POSTFIX);
                Optional<String> x = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_X_POSTFIX);
                Optional<String> y = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_Y_POSTFIX);
                Optional<String> x5c = configUtil.get(ConfigKeys.JWT_KEYS + "[" + i + "]." + ConfigKeys.KEY_X5C_POSTFIX);
                
                boolean hasKey = false;
                if (x5c.isPresent()) {
                    jsonWebKey.setX5c(List.of(x5c.get()));
                    hasKey = true;
                } else if (n.isPresent() && e.isPresent()) {
                    jsonWebKey.setN(n.get());
                    jsonWebKey.setE(e.get());
                    hasKey = true;
                } else if (secret.isPresent()) {
                    jsonWebKey.setSecret(secret.get());
                    hasKey = true;
                } else if (x.isPresent() && y.isPresent() && crv.isPresent()) {
                    jsonWebKey.setX(x.get());
                    jsonWebKey.setY(y.get());
                    jsonWebKey.setCrv(crv.get());
                    hasKey = true;
                }
                
                if (hasKey) {
                    jsonWebKeys.add(jsonWebKey);
                }
            }
        }
        
        return jsonWebKeys;
    }
    
}
