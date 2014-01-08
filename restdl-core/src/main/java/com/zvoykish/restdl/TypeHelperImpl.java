package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 01:10
 */
public class TypeHelperImpl implements TypeHelper {
    private RestdlAdapter adapter;

    public TypeHelperImpl(RestdlAdapter adapter) {
        this.adapter = adapter;
    }

    public TypedObject typeToAType(Type paramType) {
        if (paramType instanceof Class) {
            Class clazz = (Class) paramType;
            if (clazz.isInterface()) {
                Class<?> targetClass = adapter.resolveTargetClass(clazz);
                if (!clazz.equals(targetClass)) {
                    return typeToAType(targetClass);
                }
            }

            Package pkg = clazz.getPackage();
            String basePackage = adapter.getBasePackage();
            if (!clazz.isEnum() && !clazz.isPrimitive() && pkg != null &&
                    (basePackage == null || pkg.getName().startsWith(basePackage)))
            {
                Field[] allFields = clazz.getDeclaredFields();
                return new ComplexObject(clazz.getName(), fieldsToAnObjects(allFields));
            }
            else {
                return new PrimitiveObject(clazz.getName());
            }
        }
        else if (paramType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) paramType;
            Type rawType = parameterizedType.getRawType();
            Type[] types = parameterizedType.getActualTypeArguments();
            if (Collection.class.isAssignableFrom((Class) rawType)) {
                return new CollectionObject(typeToAType(types[0]));
            }
            else if (Map.class.isAssignableFrom((Class) rawType)) {
                return new MapObject(typeToAType(types[0]), typeToAType(types[1]));
            }
            else {
                return typeToAType(types[0]);
            }
        }

        Field[] allFields = paramType.getClass().getDeclaredFields();
        return new ComplexObject(paramType.getClass().getName(), fieldsToAnObjects(allFields));
    }

    public List<AnObject> fieldsToAnObjects(Field[] allFields) {
        List<AnObject> result = new ArrayList<>();
        for (Field field : allFields) {
            result.add(new AnObject(field.getName(), typeToAType(field.getGenericType())));
        }
        return result;
    }
}
