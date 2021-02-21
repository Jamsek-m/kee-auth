package com.mjamsek.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonWebKeySet {
    
    @JsonProperty("keys")
    private List<JsonWebKey> keys;
    
    public List<JsonWebKey> getKeys() {
        return keys;
    }
    
    public void setKeys(List<JsonWebKey> keys) {
        this.keys = keys;
    }
    
}
