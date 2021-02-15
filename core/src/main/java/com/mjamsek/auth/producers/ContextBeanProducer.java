package com.mjamsek.auth.producers;

import com.mjamsek.auth.common.annotations.Token;
import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.utils.TokenUtil;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.Optional;

@RequestScoped
public class ContextBeanProducer {
    
    @Context
    private HttpServletRequest httpRequest;
    
    @Produces
    @RequestScoped
    public AuthContext produceContext() {
        return getAuthorizationHeaderValue()
            .map(ContextProducer::produceContext)
            .orElse(ContextProducer.produceEmptyContext());
    }
    
    @Produces
    @Token
    public Optional<String> produceRawToken() {
        return getAuthorizationHeaderValue();
    }
    
    private Optional<String> getAuthorizationHeaderValue() {
        return Optional.ofNullable(httpRequest.getHeader(HttpHeaders.AUTHORIZATION))
            .map(TokenUtil::trimAuthorizationHeader);
    }
    
}
