package com.zvoykish.restdl.adapters.partials;

import com.zvoykish.restdl.BasePartialAdapter;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 01:01
 */
public class PartialCodehausJacksonAdapter extends BasePartialAdapter {
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
