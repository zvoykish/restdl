package com.zvoykish.restdl.generator.java.impl;

import com.zvoykish.restdl.generator.ContentGenerator;
import com.zvoykish.restdl.objects.PrimitiveObject;
import com.zvoykish.restdl.objects.TypedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 19:44
 */
public class PrimitiveObjectContentGenerator implements ContentGenerator<PrimitiveObject> {
    private static final List<Pattern> IGNORED_CLASSES = new ArrayList<>();

    static {
        IGNORED_CLASSES.add(Pattern.compile("(void)|(long)|(int)(boolean)|(float)|(double)|(short)|(byte)"));
        IGNORED_CLASSES.add(Pattern.compile("java..*"));
    }

    @Override
    public String generateContent(PrimitiveObject object, String className, Map<Long, TypedObject> typeMap) {
        for (Pattern ignoredClass : IGNORED_CLASSES) {
            if (ignoredClass.matcher(object.getClassName()).find()) {
                return null;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("public class ").append(className).append(" {").append(EOL_CODE);
        sb.append('}').append(EOL_CODE);
        return sb.toString();
    }
}
