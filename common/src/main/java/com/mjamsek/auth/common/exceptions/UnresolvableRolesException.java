package com.mjamsek.auth.common.exceptions;

public class UnresolvableRolesException extends Exception {
    
    private String resolverName;
    
    public UnresolvableRolesException(String resolverName) {
        super();
        this.resolverName = resolverName;
    }
    
    public UnresolvableRolesException(String resolverName, String message) {
        super(message);
        this.resolverName = resolverName;
    }
    
    public UnresolvableRolesException(String resolverName, String message, Throwable cause) {
        super(message, cause);
        this.resolverName = resolverName;
    }
    
    public UnresolvableRolesException(String resolverName, Throwable cause) {
        super(cause);
        this.resolverName = resolverName;
    }
    
    protected UnresolvableRolesException(String resolverName, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.resolverName = resolverName;
    }
    
    public String getResolverName() {
        return resolverName;
    }
    
    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
    }
}
