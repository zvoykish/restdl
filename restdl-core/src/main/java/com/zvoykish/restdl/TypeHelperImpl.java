package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.types.*;

import java.lang.annotation.Annotation;
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
    private RestdlAdapter adapter;

    public TypeHelperImpl(RestdlAdapter adapter) {
        this.adapter = adapter;
    }

    public TypedObject typeToAType(Type paramType, Map<String, AtomicReference<TypedObject>> objects) {
        String className = resolveClassName(paramType);
        if (objects.containsKey(className)) {
            return new TypedObjectWrapper(objects.get(className));
        }
        AtomicReference<TypedObject> objectReference = new AtomicReference<>();
        objects.put(className, objectReference);

        TypedObject object = null;
        try {
            if (paramType instanceof Class) {
                Class clazz = (Class) paramType;
                if (clazz.isInterface()) {
                    Class<?> targetClass = adapter.resolveTargetClass(clazz);
                    if (!clazz.equals(targetClass)) {
                        return typeToAType(targetClass, objects);
                    }
                }

                if (clazz.isArray()) {
                    Class componentClass = clazz.getComponentType();
                    TypedObject componentType = typeToAType(componentClass, objects);
                    object = new ArrayObject(className, componentType);
                    return getReferencedObject(object);
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
                    object = new CollectionObject(Collection.class.getName(), innerObject);
                    return getReferencedObject(object);
                }
                else if (Map.class.isAssignableFrom(rawClass)) {
                    TypedObject keyObject = typeToAType(types[0], objects);
                    TypedObject valueObject = typeToAType(types[1], objects);
                    object = new MapObject(Map.class.getName(), keyObject, valueObject);
                    return getReferencedObject(object);
                }
                else {
                    TypedObject rawObject = typeToAType(rawType, objects);
                    if (!(rawObject instanceof ReferencedTypedObject)) {
                        rawObject = rawObject.toReference();
                    }

                    List<TypedObject> parametrizedObjects = new ArrayList<>();
                    for (Type type : types) {
                        parametrizedObjects.add(typeToAType(type, objects));
                    }
                    object = new ParametrizedComplexObject(className, rawObject, parametrizedObjects);
                    return getReferencedObject(object);
                }
            }
            else if (paramType instanceof TypeVariable) {
                object = new GenericDeclarationObject(((TypeVariable) paramType).getName());
                return getReferencedObject(object);
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
                objectReference.set(object);
            } else {
                objects.remove(className);
            }
        }
    }

    private String resolveClassName(Type paramType) {
        if (paramType instanceof Class) {
            Class paramClass = (Class) paramType;
            if (paramClass.isArray()) {
                return paramClass.getCanonicalName();
            }
            else {
                return paramClass.getName();
            }
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

        // Doing that filter since it's possible to inherit fields with the same name that actually collide when they're all declared as a flat list of a class
        Field[] localFields = clazz.getDeclaredFields();
        Field[] inheritedFields = getClassFields(clazz.getSuperclass());
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : localFields) {
            fieldMap.put(field.getName(), field);
        }

        for (Field field : inheritedFields) {
            String fieldName = field.getName();
            if (!fieldMap.containsKey(fieldName)) {
                fieldMap.put(fieldName, field);
            }
        }

        List<Field> fields = new ArrayList<>();
        fields.addAll(fieldMap.values());
        Collections.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

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
                !field.isSynthetic() &&
                !hasJsonIgnoreAnnotation(field);
    }

    private boolean hasJsonIgnoreAnnotation(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> clazz = annotation.annotationType();
            if (com.fasterxml.jackson.annotation.JsonIgnore.class.equals(clazz) ||
                    org.codehaus.jackson.annotate.JsonIgnore.class.equals(clazz))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isInlineTypes() {
        return Boolean.TRUE.equals(INLINE_TYPES.get());
    }
}
