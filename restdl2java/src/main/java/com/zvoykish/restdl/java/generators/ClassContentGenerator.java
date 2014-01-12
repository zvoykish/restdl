package com.zvoykish.restdl.java.generators;

import com.zvoykish.restdl.objects.ComplexObject;
import com.zvoykish.restdl.objects.ParametrizedComplexObject;
import com.zvoykish.restdl.objects.TypedObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 20:26
 */
public class ClassContentGenerator implements ContentGenerator<TypedObject> {
    private ComplexObjectGenerator complexObjectGenerator = new ComplexObjectGenerator();

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
