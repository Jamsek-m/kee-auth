/*
 *  Copyright (c) 2019-2021 Miha Jamsek and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.mjamsek.auth.producers;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.annotations.Token;
import com.mjamsek.auth.context.AuthContext;
import com.mjamsek.auth.utils.TokenUtil;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.Optional;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
@RequestScoped
public class ContextBeanProducer {
    
    private static final String LOCATION_HEADER = "header";
    private static final String LOCATION_COOKIE = "cookie";
    
    @Context
    private HttpServletRequest httpRequest;
    
    @Produces
    @RequestScoped
    public AuthContext produceContext() {
        return getCredentials()
            .map(ContextProducer::produceContext)
            .orElse(ContextProducer.produceEmptyContext());
    }
    
    @Produces
    @Token
    public Optional<String> produceRawToken() {
        return getCredentials();
    }
    
    private Optional<String> getCredentials() {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        String location = configUtil.get("kee-auth.oidc.credentials.location").orElse(LOCATION_HEADER);
        
        if (location.equals(LOCATION_COOKIE)) {
            String cookieName = configUtil.get("kee-auth.oidc.credentials.cookie-name")
                .orElse("authorization");
            return this.getCookieValue(cookieName);
        }
        String headerName = configUtil.get("kee-auth.oidc.credentials.header-name")
            .orElse(HttpHeaders.AUTHORIZATION);
        return this.getHeaderValue(headerName);
    }
    
    private Optional<String> getHeaderValue(String headerName) {
        return Optional.ofNullable(httpRequest.getHeader(headerName))
            .map(TokenUtil::trimAuthorizationHeader);
    }
    
    private Optional<String> getCookieValue(String cookieName) {
        Cookie[] cookies = httpRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(cookieName)) {
                return Optional.of(cookie.getValue()).map(TokenUtil::trimAuthorizationHeader);
            }
        }
        return Optional.empty();
    }
    
}
