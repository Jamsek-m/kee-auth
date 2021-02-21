package com.mjamsek.auth.common.enums;

import java.util.Set;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public enum VerificationAlgorithm {
    RS256,
    RS384,
    RS512,
    ES256,
    ES384,
    ES512,
    HS256,
    HS384,
    HS512;
    
    /**
     * Checks whether this algorithm is equal to any element in the provided set
     * @param equalsToOneOf set of algorithms to be compared to
     * @return true if at least one element matches this, false otherwise
     */
    public boolean isPartOfSet(Set<VerificationAlgorithm> equalsToOneOf) {
        return equalsToOneOf.stream().anyMatch(alg -> alg.name().equals(this.name()));
    }
}
