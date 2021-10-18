package com.mjamsek.auth.common.config;

public class OIDCConstants {
    
    public static final String GRANT_TYPE_PARAM = "grant_type";
    public static final String GRANT_TYPE_CLIENT_CREDENTIALS_VALUE = "client_credentials";
    public static final String AUTHORIZATION_BASIC_PREFIX = "Basic";
    
    public static final String JWT_SUB_CLAIM = "sub";
    public static final String JWT_PREFERRED_USERNAME_CLAIM = "preferred_username";
    public static final String JWT_EMAIL_CLAIM = "email";
    public static final String JWT_SCOPE_CLAIM = "scope";
    
    public static final String CREDENTIALS_LOCATION_HEADER = "header";
    public static final String CREDENTIALS_LOCATION_COOKIE = "cookie";
    
}
