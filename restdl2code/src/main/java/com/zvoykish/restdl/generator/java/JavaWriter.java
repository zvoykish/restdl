package com.zvoykish.restdl.generator.java;

import com.zvoykish.restdl.objects.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/12/14
 * Time: 22:34
 */
public class JavaWriter {
    public String resolveTypeName(TypedObject type, Map<Long, TypedObject> typeMap) {
        String className = type.getClassName();
        if (type instanceof CollectionObject) {
            TypedObject elementType = ((CollectionObject) type).getElementType();
            TypedObject resolvedType = typeMap.get(elementType.getId());
            return className + '<' + resolveTypeName(resolvedType, typeMap) + '>';
        }
        else if (type instanceof MapObject) {
            TypedObject keyType = ((MapObject) type).getKeyType();
            TypedObject valueType = ((MapObject) type).getValueType();
            TypedObject resolvedKeyType = typeMap.get(keyType.getId());
            TypedObject resolvedValueType = typeMap.get(valueType.getId());
            return className + '<' + resolveTypeName(resolvedKeyType, typeMap) + ", " +
                    resolveTypeName(resolvedValueType, typeMap) + '>';
        } else if (type instanceof ArrayObject) {
            TypedObject componentType = ((ArrayObject) type).getComponent();
            TypedObject resolvedComponentType = typeMap.get(componentType.getId());
            return resolveTypeName(resolvedComponentType, typeMap) + "[]";
        }
        else if (!className.contains(".") && !"void".equals(className) && !(type instanceof GenericDeclarationObject)) {
            // Handle primitive data types (long, boolean, etc.)
            return Character.toUpperCase(className.charAt(0)) + className.substring(1);
        }

        return className.replace('$', '_');
    }
}
