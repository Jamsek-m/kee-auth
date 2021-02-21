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
