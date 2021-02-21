package com.mjamsek.auth.common.exceptions;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class MissingConfigException extends RuntimeException {
    
    public MissingConfigException(String key) {
        super("Missing configuration key: '" + key + "'! Invoked action depends on this value be present.");
    }
    
    public MissingConfigException(String key, Throwable cause) {
        super("Missing configuration key: '" + key + "'! Invoked action depends on this value be present.", cause);
    }
    
    protected MissingConfigException(String key, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("Missing configuration key: '" + key + "'! Invoked action depends on this value be present.", cause, enableSuppression, writableStackTrace);
    }
}
