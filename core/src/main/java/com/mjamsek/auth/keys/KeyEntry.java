package com.mjamsek.auth.keys;

import com.nimbusds.jose.JWSAlgorithm;

import java.io.Serializable;
import java.security.Key;
import java.util.Objects;

public class KeyEntry implements Serializable {
    
    private final String kid;
    
    private JWSAlgorithm algorithm;
    
    private Key verificationKey;
    
    public KeyEntry(String kid) {
        this.kid = kid;
    }
    
    public String getKid() {
        return kid;
    }
    
    public void setAlgorithm(JWSAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    public JWSAlgorithm getAlgorithm() {
        return algorithm;
    }
    
    public void setVerificationKey(Key verificationKey) {
        this.verificationKey = verificationKey;
    }
    
    public Key getVerificationKey() {
        return verificationKey;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyEntry keyEntry = (KeyEntry) o;
        return kid.equals(keyEntry.kid);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(kid);
    }
}
