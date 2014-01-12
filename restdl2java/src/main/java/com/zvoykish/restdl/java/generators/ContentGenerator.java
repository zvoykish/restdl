package com.zvoykish.restdl.java.generators;

import com.zvoykish.restdl.objects.TypedObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 19:07
 */
public interface ContentGenerator<T extends TypedObject> {
    String generateContent(T object, String className, Map<Long, TypedObject> typeMap);

    public static final String EOL = "\n";
    public static final String EOL_CODE = ';' + EOL;
}
