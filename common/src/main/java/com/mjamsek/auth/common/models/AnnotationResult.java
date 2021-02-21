package com.mjamsek.auth.common.models;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class AnnotationResult<T> {
    
    private final T annotation;
    
    private final boolean classAnnotated;
    
    public AnnotationResult(T annotation, boolean classAnnotated) {
        this.annotation = annotation;
        this.classAnnotated = classAnnotated;
    }
    
    public T getAnnotation() {
        return this.annotation;
    }
    
    public boolean hasAnnotation() {
        return this.annotation != null;
    }
    
    public boolean isClassAnnotated() {
        return this.classAnnotated;
    }
    
}
