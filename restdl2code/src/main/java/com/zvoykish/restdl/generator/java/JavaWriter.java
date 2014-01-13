package com.zvoykish.restdl.generator.java;

import com.zvoykish.restdl.objects.CollectionObject;
import com.zvoykish.restdl.objects.MapObject;
import com.zvoykish.restdl.objects.TypedObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/12/14
 * Time: 22:34
 */
public class JavaWriter {
    public String resolveTypeName(TypedObject type, Map<Long, TypedObject> typeMap) {
        if (type instanceof CollectionObject) {
            TypedObject elementType = ((CollectionObject) type).getElementType();
            TypedObject resolvedType = typeMap.get(elementType.getId());
            return type.getClassName() + '<' + resolveTypeName(resolvedType, typeMap) + '>';
        }
        else if (type instanceof MapObject) {
            TypedObject keyType = ((MapObject) type).getKeyType();
            TypedObject valueType = ((MapObject) type).getValueType();
            TypedObject resolvedKeyType = typeMap.get(keyType.getId());
            TypedObject resolvedValueType = typeMap.get(valueType.getId());
            return type.getClassName() + '<' + resolveTypeName(resolvedKeyType, typeMap) + ", " +
                    resolveTypeName(resolvedValueType, typeMap) + '>';
        }

        return type.getClassName().replace('$', '_');
    }
}
