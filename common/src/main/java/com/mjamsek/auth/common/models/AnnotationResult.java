package com.mjamsek.auth.common.models;

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
