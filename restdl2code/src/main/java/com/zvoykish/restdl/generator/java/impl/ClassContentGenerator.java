package com.zvoykish.restdl.generator.java.impl;

import com.zvoykish.restdl.generator.ContentGenerator;
import com.zvoykish.restdl.generator.java.JavaWriter;
import com.zvoykish.restdl.objects.types.ComplexObject;
import com.zvoykish.restdl.objects.types.ParametrizedComplexObject;
import com.zvoykish.restdl.objects.types.TypedObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 20:26
 */
public class ClassContentGenerator implements ContentGenerator<TypedObject> {
    private ComplexObjectGenerator complexObjectGenerator;

    public ClassContentGenerator(JavaWriter writer) {
        complexObjectGenerator = new ComplexObjectGenerator(writer);
    }

    @Override
    public String generateContent(TypedObject object, String className, Map<Long, TypedObject> typeMap) {
        if (object instanceof ParametrizedComplexObject) {
            System.out.println("Not yet..... :(");
        }
        else if (object instanceof ComplexObject) {
            return complexObjectGenerator.generateContent((ComplexObject) object, className, typeMap);
        }

        return null;
    }
}
