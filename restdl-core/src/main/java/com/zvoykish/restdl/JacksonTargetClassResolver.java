package com.zvoykish.restdl;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/19/14
 * Time: 01:07
 */
public class JacksonTargetClassResolver implements TargetClassResolver {
    @Override
    public Class<?> resolveTargetClass(Class<?> clazz) {
        Class<?> result = clazz;
        if (clazz.isAnnotationPresent(com.fasterxml.jackson.databind.annotation.JsonDeserialize.class)) {
            com.fasterxml.jackson.databind.annotation.JsonDeserialize annotation = clazz
                    .getAnnotation(com.fasterxml.jackson.databind.annotation.JsonDeserialize.class);
            if (annotation != null) {
                result = annotation.as();
            }
        }
        else if (clazz.isAnnotationPresent(org.codehaus.jackson.map.annotate.JsonDeserialize.class)) {
            org.codehaus.jackson.map.annotate.JsonDeserialize annotation = clazz
                    .getAnnotation(org.codehaus.jackson.map.annotate.JsonDeserialize.class);
            if (annotation != null) {
                result = annotation.as();
            }
        }

        return result;
    }
}
