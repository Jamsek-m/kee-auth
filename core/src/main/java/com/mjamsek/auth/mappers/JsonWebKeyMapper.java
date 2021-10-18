package com.mjamsek.auth.mappers;

import com.mjamsek.auth.keys.KeyBuilder;
import com.mjamsek.auth.keys.KeyEntry;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;

import java.util.Map;

import static com.mjamsek.auth.common.config.KeyConstants.*;

public class JsonWebKeyMapper {
    
    public static KeyEntry toKeyEntry(JWK jsonWebKey) {
        KeyBuilder builder = KeyBuilder.newBuilder(jsonWebKey.getKeyID());
        JWSAlgorithm algorithm = JWSAlgorithm.parse(jsonWebKey.getAlgorithm().getName());
        
        Map<String, ?> keyParams = jsonWebKey.getRequiredParams();
        if (JWSAlgorithm.Family.RSA.contains(algorithm)) {
            String n = (String) keyParams.get(RSA_N_PARAM);
            String e = (String) keyParams.get(RSA_E_PARAM);
            return builder.withRsaAlgorithm(algorithm)
                .withModulusAndExponent(n, e)
                .build();
        } else if (JWSAlgorithm.Family.EC.contains(algorithm)) {
            String x = (String) keyParams.get(EC_X_PARAM);
            String y = (String) keyParams.get(EC_Y_PARAM);
            String crv = (String) keyParams.get(EC_CRV_PARAM);
            return builder.withECAlgorithm(algorithm)
                .withCurveParameters(crv, x, y)
                .build();
        } else {
            throw new RuntimeException("Unknown alg!");
        }
    }
    
}
