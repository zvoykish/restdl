package com.zvoykish.restdl.adapters.partials;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zvoykish.restdl.BasePartialAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 02:37
 */
public class PartialFasterXmlJacksonAdapter extends BasePartialAdapter {
    public Class<?> resolveTargetClass(Class<?> clazz) {
        Class<?> result = clazz;
        if (clazz.isAnnotationPresent(JsonDeserialize.class)) {
            JsonDeserialize annotation = clazz.getAnnotation(JsonDeserialize.class);
            if (annotation != null) {
                result = annotation.as();
            }
        }

        return result;
    }
}
