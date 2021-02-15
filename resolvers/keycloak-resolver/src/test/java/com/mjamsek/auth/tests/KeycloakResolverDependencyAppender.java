package com.mjamsek.auth.tests;

import com.kumuluz.ee.testing.arquillian.spi.MavenDependencyAppender;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class KeycloakResolverDependencyAppender implements MavenDependencyAppender {
    
    private static final ResourceBundle versionsBundle = ResourceBundle.getBundle("META-INF/kumuluzee/versions");
    
    @Override
    public ConfigurableMavenResolverSystem configureResolver(ConfigurableMavenResolverSystem resolver) {
        resolver.loadPomFromFile("pom.xml").importDependencies(ScopeType.RUNTIME);
        return resolver;
    }
    
    @Override
    public List<String> addLibraries() {
        List<String> libs = new ArrayList<>();
        
        libs.add("com.kumuluz.ee:kumuluzee-jax-rs-jersey:");
        libs.add("com.fasterxml.jackson.core:jackson-databind:" + versionsBundle.getString("jackson-version"));
        libs.add("io.jsonwebtoken:jjwt-impl:" + versionsBundle.getString("jjwt-version"));
        
        return libs;
    }
}
