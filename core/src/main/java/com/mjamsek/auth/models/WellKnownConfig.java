package com.mjamsek.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WellKnownConfig {
    
    public static final String ISSUER = "issuer";
    public static final String AUTHORIZATION_ENDPOINT = "authorization_endpoint";
    public static final String TOKEN_ENDPOINT = "token_endpoint";
    public static final String USERINFO_ENDPOINT = "userinfo_endpoint";
    public static final String JWKS_URI = "jwks_uri";
    public static final String ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED = "id_token_signing_alg_values_supported";
    
    @JsonProperty(ISSUER)
    private String issuer;
    
    @JsonProperty(AUTHORIZATION_ENDPOINT)
    private String authorizationEndpoint;
    
    @JsonProperty(TOKEN_ENDPOINT)
    private String tokenEndpoint;
    
    @JsonProperty(USERINFO_ENDPOINT)
    private String userInfoEndpoint;
    
    @JsonProperty(JWKS_URI)
    private String jwksUri;
    
    @JsonProperty(ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED)
    private List<String> idTokenSigningAlgValuesSupported;
    
    public String getIssuer() {
        return issuer;
    }
    
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    
    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }
    
    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }
    
    public String getTokenEndpoint() {
        return tokenEndpoint;
    }
    
    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }
    
    public String getUserInfoEndpoint() {
        return userInfoEndpoint;
    }
    
    public void setUserInfoEndpoint(String userInfoEndpoint) {
        this.userInfoEndpoint = userInfoEndpoint;
    }
    
    public String getJwksUri() {
        return jwksUri;
    }
    
    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }
    
    public List<String> getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }
    
    public void setIdTokenSigningAlgValuesSupported(List<String> idTokenSigningAlgValuesSupported) {
        this.idTokenSigningAlgValuesSupported = idTokenSigningAlgValuesSupported;
    }
}
