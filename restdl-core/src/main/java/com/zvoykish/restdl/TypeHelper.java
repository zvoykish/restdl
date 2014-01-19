package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.types.TypedObject;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 01:10
 */
public interface TypeHelper {
    public static ThreadLocal<Boolean> INLINE_TYPES = new ThreadLocal<>();

    TypedObject typeToAType(Type paramType, Map<String, AtomicReference<TypedObject>> objects);
}
