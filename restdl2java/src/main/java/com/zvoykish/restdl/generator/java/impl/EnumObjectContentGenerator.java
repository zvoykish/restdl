package com.zvoykish.restdl.generator.java.impl;

import com.zvoykish.restdl.generator.ContentGenerator;
import com.zvoykish.restdl.objects.EnumObject;
import com.zvoykish.restdl.objects.TypedObject;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 19:10
 */
public class EnumObjectContentGenerator implements ContentGenerator<EnumObject> {

    @Override
    public String generateContent(EnumObject object, String className, Map<Long, TypedObject> typeMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("public enum ").append(className).append(" {").append(EOL);
        List<String> constants = object.getConstants();
        for (int i = 0; i < constants.size(); i++) {
            sb.append('\t');
            sb.append(constants.get(i).replace("\"", ""));
            if (i < constants.size() - 1) {
                sb.append(',');
            }
            sb.append(EOL);
        }
        sb.append('}').append(EOL);
        return sb.toString();
    }
}
