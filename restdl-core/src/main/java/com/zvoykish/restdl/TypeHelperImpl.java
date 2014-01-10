package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 01:10
 */
public class TypeHelperImpl implements TypeHelper {
    private static final List<String> VOID_TYPES = new ArrayList<>();
    private RestdlAdapter adapter;

    static {
        VOID_TYPES.add("void");
        VOID_TYPES.add("java.lang.Void");
    }

    public TypeHelperImpl(RestdlAdapter adapter) {
        this.adapter = adapter;
    }

    public TypedObject typeToAType(Type paramType, Map<String, AtomicReference<TypedObject>> objects) {
        String className = resolveClassName(paramType);
        if (objects.containsKey(className)) {
            return getReferencedObject(objects.get(className).get());
        }
        objects.put(className, new AtomicReference<TypedObject>());

        TypedObject object = null;
        try {
            if (paramType instanceof Class) {
                Class clazz = (Class) paramType;
                if (clazz.isInterface()) {
                    Class<?> targetClass = adapter.resolveTargetClass(clazz);
                    if (!clazz.equals(targetClass)) {
                        object = typeToAType(targetClass, objects);
                        return getReferencedObject(object);
                    }
                }

                Package pkg = clazz.getPackage();
                String basePackage = adapter.getBasePackage();
                if (clazz.isEnum()) {
                    List<String> constants = new ArrayList<>();
                    for (Enum anEnum : (Enum[]) clazz.getEnumConstants()) {
                        constants.add(anEnum.name());
                    }
                    object = new EnumObject(className, constants);
                    return getReferencedObject(object);
                }
                else if (!clazz.isPrimitive() && pkg != null &&
                        (basePackage == null || pkg.getName().startsWith(basePackage)))
                {
                    Field[] allFields = getClassFields(clazz);
                    object = new ComplexObject(className, fieldsToAnObjects(allFields, objects));
                    return getReferencedObject(object);
                }
                else {
                    object = new PrimitiveObject(className);
                    return getReferencedObject(object);
                }
            }
            else if (paramType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) paramType;
                Type rawType = parameterizedType.getRawType();
                Class rawClass = (Class) rawType;
                Type[] types = parameterizedType.getActualTypeArguments();

                if (Collection.class.isAssignableFrom(rawClass)) {
                    TypedObject innerObject = typeToAType(types[0], objects);
                    object = new CollectionObject(innerObject);
                    return getReferencedObject(object);
                }
                else if (Map.class.isAssignableFrom(rawClass)) {
                    TypedObject innerObject = typeToAType(types[0], objects);
                    object = new MapObject(innerObject, typeToAType(types[1], objects));
                    return getReferencedObject(object);
                }
                else {
                    TypedObject rawObject = typeToAType(rawType, objects);
                    List<TypedObject> parametrizedObjects = new ArrayList<>();
                    for (Type type : types) {
                        parametrizedObjects.add(typeToAType(type, objects));
                    }
                    object = new ParametrizedComplexObject(className, (ReferencedTypedObject) rawObject,
                            parametrizedObjects);
                    return getReferencedObject(object);
                }
            }
            else {
                Class<? extends Type> paramTypeClass = paramType.getClass();
                Field[] allFields = getClassFields(paramTypeClass);
                object = new ComplexObject(className, fieldsToAnObjects(allFields, objects));
                return getReferencedObject(object);
            }
        }
        finally {
            if (className != null && object != null) {
                objects.get(className).set(object);
            }
        }
    }

    private String resolveClassName(Type paramType) {
        if (paramType instanceof Class) {
            return ((Class) paramType).getName();
        }
        else if (paramType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) paramType;
            Type rawType = parameterizedType.getRawType();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            StringBuilder sb = new StringBuilder();
            sb.append(resolveClassName(rawType)).append('<');
            for (int i = 0; i < actualTypeArguments.length; i++) {
                sb.append(resolveClassName(actualTypeArguments[i]));
                if (i < actualTypeArguments.length - 1) {
                    sb.append(", ");
                }
            }
            return sb.append('>').toString();
        }
        else if (paramType instanceof GenericArrayType) {
            Type genericType = ((GenericArrayType) paramType).getGenericComponentType();
            return resolveClassName(genericType) + "[]";
        }
        else {
            return paramType.toString();
        }
    }

    private List<AnObject> fieldsToAnObjects(Field[] allFields, Map<String, AtomicReference<TypedObject>> objects) {
        List<AnObject> result = new ArrayList<>();
        for (Field field : allFields) {
            if (isEligible(field)) {
                result.add(new AnObject(field.getName(), typeToAType(field.getGenericType(), objects)));
            }
        }
        return result;
    }

    private Field[] getClassFields(Class clazz) {
        if (clazz == null || !clazz.getPackage().getName().startsWith(adapter.getBasePackage())) {
            return new Field[0];
        }

        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        fields.addAll(Arrays.asList(getClassFields(clazz.getSuperclass())));
        return fields.toArray(new Field[fields.size()]);
    }

    private TypedObject getReferencedObject(TypedObject obj) {
        return isInlineTypes() || obj == null ?
                obj :
                obj.toReference();
    }

    private boolean isEligible(Field field) {
        int modifiers = field.getModifiers();
        return !Modifier.isStatic(modifiers) &&
                !Modifier.isTransient(modifiers) &&
                !field.isSynthetic();
    }

    private boolean isInlineTypes() {
        return Boolean.TRUE.equals(INLINE_TYPES.get());
    }
}
