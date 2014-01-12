package com.zvoykish.restdl.java.generators;

import com.zvoykish.restdl.objects.*;

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
        sb.append("public class ").append(effectiveClassName).append(" {").append(EOL);
        for (AnObject fieldObject : object.getFields()) {
            sb.append('\t');
            sb.append("public ");
            sb.append(resolveType(fieldObject, typeMap));
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

    private String resolveType(AnObject fieldObject, Map<Long, TypedObject> typeMap) {
        TypedObject type = fieldObject.getType();
        if (type instanceof CollectionObject) {
            TypedObject elementType = ((CollectionObject) type).getElementType();
            if (elementType != null) {
                String elementClassName = typeMap.get(elementType.getId()).getClassName();
                return type.getClassName() + '<' + elementClassName + '>';
            }
        }
        else if (type instanceof MapObject) {
            TypedObject keyType = ((MapObject) type).getKeys();
            TypedObject valueType = ((MapObject) type).getValues();
            if (keyType != null && valueType != null) {
                String keyClassName = typeMap.get(keyType.getId()).getClassName();
                String valueClassName = typeMap.get(valueType.getId()).getClassName();
                return type.getClassName() + '<' + keyClassName + ", " + valueClassName + '>';
            }
        }

        return type.getClassName().replace('$', '_');
    }
}
