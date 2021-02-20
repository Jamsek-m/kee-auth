package com.mjamsek.auth.common.exceptions;

public class UnrecognizedKeyException extends RuntimeException {
    
    public UnrecognizedKeyException() {
        super();
    }
    
    public UnrecognizedKeyException(String message) {
        super(message);
    }
    
    public UnrecognizedKeyException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UnrecognizedKeyException(Throwable cause) {
        super(cause);
    }
    
    protected UnrecognizedKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}