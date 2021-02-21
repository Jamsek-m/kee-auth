package com.mjamsek.auth.models.keys;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public abstract class JwtSigningKey {
    
    protected final String kid;
    
    protected JwtSigningKey(String kid) {
        this.kid = kid;
    }
    
    public String getKid() {
        return kid;
    }
    
}
