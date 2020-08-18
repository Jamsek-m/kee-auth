package com.mjamsek.auth.keycloak.apis;

import javax.json.bind.annotation.JsonbProperty;

public class KeycloakErrorResponse {

    @JsonbProperty("error")
    private String errorCode;
    
    @JsonbProperty("error_description")
    private String errorDescription;
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public KeycloakErrorResponse setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }
    
    public String getErrorDescription() {
        return errorDescription;
    }
    
    public KeycloakErrorResponse setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }
    
    enum Code {
        UNAUTHORIZED_CLIENT("unauthorized_client"),
        UNKNOWN_ERROR("unknown_error"),
        GENERIC_ERROR("");
        
        private String value;
        
        Code(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
        
        @Override
        public String toString() {
            return this.getValue();
        }
    
        /**
         * Maps string value to enum value. If value is not recognized, it returns generic error.
         * @param value string value
         * @return enum field
         */
        public static Code fromValue(String value) {
            for (Code code : values()) {
                if (code.getValue().equals(value)) {
                    return code;
                }
            }
            return Code.GENERIC_ERROR;
        }
    }
}
