package com.mjamsek.auth.common.config;

import com.mjamsek.auth.common.enums.VerificationAlgorithm;

import java.util.List;

public class DefaultConfig {
    
    public static final List<VerificationAlgorithm> defaultSupportedAlgs = List.of(
        VerificationAlgorithm.RS256
    );
    
}
