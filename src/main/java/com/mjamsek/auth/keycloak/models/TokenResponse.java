package com.mjamsek.auth.keycloak.models;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbProperty;

@JsonbNillable
public class TokenResponse {

    @JsonbProperty("access_token")
    private String accessToken;
    
    @JsonbProperty("expires_in")
    private Integer expiresIn;
    
    @JsonbProperty("refresh_expires_in")
    private Integer refreshExpiresIn;
    
    @JsonbProperty("refresh_token")
    private String refreshToken;
    
    @JsonbProperty("token_type")
    private String tokenType;
    
    @JsonbProperty("not-before-policy")
    private Integer notBeforePolicy;
    
    @JsonbProperty("session_state")
    private String sessionState;
    
    @JsonbProperty("scope")
    private String scope;
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public Integer getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public Integer getRefreshExpiresIn() {
        return refreshExpiresIn;
    }
    
    public void setRefreshExpiresIn(Integer refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public Integer getNotBeforePolicy() {
        return notBeforePolicy;
    }
    
    public void setNotBeforePolicy(Integer notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }
    
    public String getSessionState() {
        return sessionState;
    }
    
    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
}
