package com.mjamsek.auth.keycloak.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.keycloak.enums.VerificationAlgorithm;
import com.mjamsek.auth.keycloak.apis.KeycloakApi;
import com.mjamsek.auth.keycloak.exceptions.KeycloakConfigException;
import com.mjamsek.auth.keycloak.utils.CertUtil;
import com.mjamsek.auth.keycloak.verifiers.TokenVerifierBuilder;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.WebApplicationException;
import java.math.BigInteger;
import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class KeycloakInitializator {
    
    private static final Logger log = Logger.getLogger(KeycloakInitializator.class.getSimpleName());
    
    private static KeycloakInitializator instance = null;
    
    public static KeycloakInitializator getInstance() {
        if (instance == null) {
            instance = new KeycloakInitializator();
        }
        return instance;
    }
    
    private KeycloakInitializator() {
    
    }
    
    public void initializeConfiguration() {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
    
        KeycloakConfig config = KeycloakConfig.getInstance();
        
        config.realm = configUtil.get(ConfigKeys.REALM_NAME)
            .orElseThrow(() -> new KeycloakConfigException("Missing 'realm' key!"));
        config.authUrl = configUtil.get(ConfigKeys.SERVER_URL)
            .orElseThrow(() -> new KeycloakConfigException("Missing 'auth-server-url' key!"));
        config.clientId = configUtil.get(ConfigKeys.CLIENT_ID)
            .orElseThrow(() -> new KeycloakConfigException("Missing 'client-id' key!"));
        config.clientSecret = configUtil.get(ConfigKeys.CLIENT_SECRET).orElse(null);
        config.leeway = configUtil.getInteger(ConfigKeys.TOKEN_LEEWAY).orElse(1000);
        
        // parse algorithm
        VerificationAlgorithm algorithm;
        Optional<String> providedAlgorithm = configUtil.get(ConfigKeys.ALGORITHM);
        if (providedAlgorithm.isPresent()) {
            try {
                algorithm = VerificationAlgorithm.valueOf(providedAlgorithm.get());
            } catch (IllegalArgumentException e) {
                log.warning("Provided invalid algorithm! Using default value of 'RS265'");
                algorithm = VerificationAlgorithm.RS265;
            }
        } else {
            log.config("No algorithm provided! Using default value of 'RS265'");
            algorithm = VerificationAlgorithm.RS265;
        }
        config.algorithm = algorithm;
        
        // parse secret key
        Optional<String> providedSecretKey = configUtil.get(ConfigKeys.SECRET_KEY);
        config.secretKey = providedSecretKey.map(CertUtil::getSecretKeyFromString).orElse(null);
        
        // parse public key
        Optional<String> providedPublicCert = configUtil.get(ConfigKeys.PUBLIC_CERT);
        config.publicKey = providedPublicCert.map(CertUtil::getPublicKeyFromCertificate).orElse(null);
        
        // register subscription to credentials
        configUtil.subscribe(ConfigKeys.CLIENT_SECRET, (key, value) -> KeycloakConfig.getInstance().clientSecret = value);
        configUtil.subscribe(ConfigKeys.SECRET_KEY, (key, value) -> KeycloakConfig.getInstance().secretKey = CertUtil.getSecretKeyFromString(value));
        configUtil.subscribe(ConfigKeys.PUBLIC_CERT, (key, value) -> KeycloakConfig.getInstance().publicKey = CertUtil.getPublicKeyFromCertificate(value));
        // set verifier strategy based on algorithm
        config.verifier = TokenVerifierBuilder.create(algorithm);
        
        // if public cert is not provided, retrieve it from server
        if (config.publicKey == null && algorithm.equals(VerificationAlgorithm.RS265)) {
            log.fine("Algorithm 'RS256' was specified, but no public key provided, downloading public certificate from Keycloak server.");
            // async
            retrievePublicKeyFromServer();
        }
    }
    
    private static void retrievePublicKeyFromServer() {
        KeycloakConfig config = KeycloakConfig.getInstance();
        
        KeycloakApi keycloakApi = RestClientBuilder
            .newBuilder()
            .baseUri(URI.create(config.authUrl))
            .build(KeycloakApi.class);
        
        final AtomicReference<Throwable> throwable = new AtomicReference<>();
        
        BiConsumer<JsonObject, Throwable> asyncCallback = (jsonResponse, err) -> {
            if (err != null) {
                log.severe(err.getMessage());
                throwable.set(err);
            }
            KeycloakConfig.KeyMeta keyMeta = parseResponse(jsonResponse);
            KeycloakConfig.getInstance().publicKey = CertUtil.getPublicKeyFromCertificate(keyMeta.getCert());
            // Use modulus and exponent instead of certificate:
            // KeycloakConfig.getInstance().publicKey = CertUtil.getPublicKeyFromModulusAndExponent(keyMeta.getModulus(), keyMeta.getExponent());
            log.info("Retrieved public certificate from Keycloak server!");
        };
        
        try {
            keycloakApi.getCertsAsync(config.realm).whenCompleteAsync(asyncCallback);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new WebApplicationException(t, 500);
        }
        
        if (throwable.get() != null) {
            throwable.get().printStackTrace();
            throw new WebApplicationException(throwable.get(), 500);
        }
    }
    
    private static KeycloakConfig.KeyMeta parseResponse(JsonObject jsonObject) {
        JsonArray keys = jsonObject.getJsonArray("keys");
        JsonObject certConfig = keys.getJsonObject(0);
        
        String kid = certConfig.getString("kid");
        String modulusStr = certConfig.getString("n");
        String exponentStr = certConfig.getString("e");
        
        BigInteger modulus = new BigInteger(1, base64Decode(modulusStr));
        BigInteger publicExponent = new BigInteger(1, base64Decode(exponentStr));
        
        JsonArray x509certs = certConfig.getJsonArray("x5c");
        JsonValue x509certValue = x509certs.get(0);
        String publicKeyCert = x509certValue.toString();
        
        KeycloakConfig.KeyMeta keyMeta = new KeycloakConfig.KeyMeta();
        keyMeta.kid = kid;
        keyMeta.modulus = modulus;
        keyMeta.exponent = publicExponent;
        keyMeta.cert = publicKeyCert;
        
        return keyMeta;
    }
    
    private static byte[] base64Decode(String base64) {
        base64 = base64.replaceAll("-", "+");
        base64 = base64.replaceAll("_", "/");
        switch (base64.length() % 4) // Pad with trailing '='s
        {
            case 0:
                break; // No pad chars in this case
            case 2:
                base64 += "==";
                break; // Two pad chars
            case 3:
                base64 += "=";
                break; // One pad char
            default:
                throw new RuntimeException(
                    "Illegal base64url string!");
        }
        return Base64.getDecoder().decode(base64);
    }
    
}
