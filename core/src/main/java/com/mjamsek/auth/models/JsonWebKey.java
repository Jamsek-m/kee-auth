package com.mjamsek.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonWebKey {
    
    @JsonProperty("kid")
    private String kid;
    
    @JsonProperty("alg")
    private String alg;
    
    @JsonProperty("n")
    private String n;
    
    @JsonProperty("e")
    private String e;
    
    @JsonProperty("crv")
    private String crv;
    
    @JsonProperty("x")
    private String x;
    
    @JsonProperty("y")
    private String y;
    
    @JsonIgnore
    private String secret;
    
    @JsonProperty("x5c")
    private List<String> x5c;
    
    public String getKid() {
        return kid;
    }
    
    public void setKid(String kid) {
        this.kid = kid;
    }
    
    public String getAlg() {
        return alg;
    }
    
    public void setAlg(String alg) {
        this.alg = alg;
    }
    
    public String getN() {
        return n;
    }
    
    public void setN(String n) {
        this.n = n;
    }
    
    public String getE() {
        return e;
    }
    
    public void setE(String e) {
        this.e = e;
    }
    
    public String getCrv() {
        return crv;
    }
    
    public void setCrv(String crv) {
        this.crv = crv;
    }
    
    public String getX() {
        return x;
    }
    
    public void setX(String x) {
        this.x = x;
    }
    
    public String getY() {
        return y;
    }
    
    public void setY(String y) {
        this.y = y;
    }
    
    public String getSecret() {
        return secret;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public List<String> getX5c() {
        return x5c;
    }
    
    public void setX5c(List<String> x5c) {
        this.x5c = x5c;
    }
    
}
