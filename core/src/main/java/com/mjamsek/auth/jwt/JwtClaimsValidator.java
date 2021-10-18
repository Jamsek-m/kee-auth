package com.mjamsek.auth.jwt;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.config.ConfigDefaults;
import com.mjamsek.auth.common.config.ConfigKeys;
import com.mjamsek.auth.config.KeeAuthConfig;
import com.mjamsek.auth.utils.DateUtil;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

public class JwtClaimsValidator {
    
    private final SignedJWT signedJWT;
    private boolean valid;
    
    public JwtClaimsValidator(SignedJWT signedJWT) {
        this.signedJWT = signedJWT;
        this.valid = true;
    }
    
    public JwtClaimsValidator checkExpiration() {
        int leewaySeconds = ConfigurationUtil.getInstance().getInteger(ConfigKeys.Jwt.TIME_LEEWAY).orElse(1);
        Date now = new Date();
        try {
            Date expiresAt = DateUtil.addToDate(signedJWT.getJWTClaimsSet().getExpirationTime(), leewaySeconds);
            boolean expired = now.after(expiresAt);
            this.valid = this.valid && !expired;
        } catch (ParseException e) {
            this.valid = false;
        }
        return this;
    }
    
    public JwtClaimsValidator checkIssuer() {
        String issuer = getIssuer();
        try {
            boolean validIssuer = signedJWT.getJWTClaimsSet().getIssuer().equals(issuer);
            this.valid = this.valid && validIssuer;
        } catch (ParseException e) {
            this.valid = false;
        }
        return this;
    }
    
    public boolean isValid() {
        return this.valid;
    }
    
    private String getIssuer() throws RuntimeException {
        return ConfigurationUtil.getInstance()
            .get(ConfigKeys.Oidc.ISSUER)
            .or(() -> {
                if (ConfigDefaults.autoconfigurationEnabled()) {
                    return KeeAuthConfig.getIssuer();
                }
                return Optional.empty();
            })
            .orElseThrow(() -> new RuntimeException("Issuer not set!"));
    }
    
}
