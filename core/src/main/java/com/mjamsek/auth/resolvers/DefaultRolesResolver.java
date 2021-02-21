package com.mjamsek.auth.resolvers;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.annotations.RolesAllowed;
import com.mjamsek.auth.common.exceptions.UnresolvableRolesException;
import com.mjamsek.auth.common.resolvers.ResolverDef;
import com.mjamsek.auth.common.resolvers.RolesResolver;
import com.mjamsek.auth.common.config.ConfigKeys;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default resolver for roles. Expects role claim to be string array (default role claim is 'roles')
 * @author Miha Jamsek
 * @since 2.0.0
 */
@ResolverDef(name = "default")
public class DefaultRolesResolver implements RolesResolver {
    
    public static final int PRIORITY = 0;
    
    @Override
    public int getPriority() {
        return PRIORITY;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Set<String> resolveRoles(Claims claims, RolesAllowed annotation) throws UnresolvableRolesException {
        Object rolesObject = claims.get(getRoleClaimName());
        if (rolesObject instanceof List) {
            List<Object> roleItemsList = (List<Object>) rolesObject;
            return roleItemsList.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
        } else {
            throw new UnresolvableRolesException(this.getName(), "Expected array of strings.");
        }
    }
    
    private String getRoleClaimName() {
        return ConfigurationUtil.getInstance().get(ConfigKeys.CLAIM_MAPPING_ROLES).orElse("roles");
    }
    
}
