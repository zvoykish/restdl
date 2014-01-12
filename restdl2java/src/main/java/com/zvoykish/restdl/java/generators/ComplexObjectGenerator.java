package com.zvoykish.restdl.java.generators;

import com.zvoykish.restdl.java.JavaWriter;
import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.ComplexObject;
import com.zvoykish.restdl.objects.GenericDeclarationObject;
import com.zvoykish.restdl.objects.TypedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 20:31
 */
public class ComplexObjectGenerator implements ContentGenerator<ComplexObject> {
    @Override
    public String generateContent(ComplexObject object, String className, Map<Long, TypedObject> typeMap) {
        StringBuilder sb = new StringBuilder();
        String effectiveClassName = getClassName(className, object.getFields());
        JavaWriter.writeClassStart(sb, effectiveClassName);
        for (AnObject fieldObject : object.getFields()) {
            sb.append('\t');
            sb.append("public ");
            JavaWriter.writeSignatureClass(sb, fieldObject.getType(), typeMap);
            sb.append(' ');
            sb.append(fieldObject.getName());
            sb.append(EOL_CODE);
        }

        sb.append('}').append(EOL);
        return sb.toString();
    }

    private String getClassName(String className, List<AnObject> fields) {
        List<String> genericObjects = new ArrayList<>();
        for (AnObject field : fields) {
            TypedObject fieldType = field.getType();
            if (fieldType instanceof GenericDeclarationObject) {
                genericObjects.add(fieldType.getClassName());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(className);
        if (!genericObjects.isEmpty()) {
            sb.append('<');
            for (int i = 0; i < genericObjects.size(); i++) {
                sb.append(genericObjects.get(i));
                if (i < genericObjects.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append('>');
        }
        return sb.toString();
    }

}
