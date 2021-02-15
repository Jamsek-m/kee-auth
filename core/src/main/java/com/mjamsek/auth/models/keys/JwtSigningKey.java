package com.mjamsek.auth.models.keys;

public abstract class JwtSigningKey {
    
    protected final String kid;
    
    protected JwtSigningKey(String kid) {
        this.kid = kid;
    }
    
    public String getKid() {
        return kid;
    }
    
}
