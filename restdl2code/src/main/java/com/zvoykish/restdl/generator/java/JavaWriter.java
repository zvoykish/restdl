package com.zvoykish.restdl.generator.java;

import com.zvoykish.restdl.objects.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/12/14
 * Time: 22:34
 */
public class JavaWriter {
    private final Map<String, String> cache = new HashMap<>();

    public String resolveTypeName(TypedObject type, Map<Long, TypedObject> typeMap) {
        String className = type.getClassName();
        if (cache.containsKey(className)) {
            return cache.get(className);
        }

        String result = null;
        try {
            if (type instanceof CollectionObject) {
                TypedObject elementType = ((CollectionObject) type).getElementType();
                TypedObject resolvedType = typeMap.get(elementType.getId());
                result = className + '<' + resolveTypeName(resolvedType, typeMap) + '>';
                return result;
            }
            else if (type instanceof MapObject) {
                TypedObject keyType = ((MapObject) type).getKeyType();
                TypedObject valueType = ((MapObject) type).getValueType();
                TypedObject resolvedKeyType = typeMap.get(keyType.getId());
                TypedObject resolvedValueType = typeMap.get(valueType.getId());
                result = className + '<' + resolveTypeName(resolvedKeyType, typeMap) + ", " +
                        resolveTypeName(resolvedValueType, typeMap) + '>';
                return result;
            }
            else if (type instanceof ArrayObject) {
                TypedObject componentType = ((ArrayObject) type).getComponent();
                TypedObject resolvedComponentType = typeMap.get(componentType.getId());
                result = resolveTypeName(resolvedComponentType, typeMap) + "[]";
                return result;
            }
            else if (!className.contains(".") && !"void".equals(className) &&
                    !(type instanceof GenericDeclarationObject))
            {
                // Handle primitive data types (long, boolean, etc.)
                result = Character.toUpperCase(className.charAt(0)) + className.substring(1);
                return result;
            }

            result = className.replace('$', '_');
            return result;
        }
        finally {
            cache.put(className, result);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public String resolveUrlWithParams(EndpointInfo endpointInfo) {
        return endpointInfo.getUrl().replace("{", "\" + ").replace("}", " + \"");
    }
}
