package com.zvoykish.restdl.generator.java;

import com.zvoykish.restdl.objects.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/12/14
 * Time: 22:34
 */
public class JavaWriter {
    private final Map<String, String> cache = new HashMap<>();
    private final static String PARAM_TEMPLATE = "{%s}";

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
        String url = endpointInfo.getUrl();
        Set<String> existingParams = new HashSet<>();
        List<AnObject> pathParams = endpointInfo.getPathParams();
        if (pathParams == null) {
            return url;
        }

        for (AnObject paramObject : pathParams) {
            existingParams.add(paramObject.getName());
        }

        String[] tokens = url.split("\\{|\\}");
        for (String token : tokens) {
            if (!token.contains("/")) {
                String paramStr = String.format(PARAM_TEMPLATE, token);
                if (existingParams.contains(token)) {
                    url = url.replace(paramStr, "\" + " + token + " + \"");
                }
                else {
                    url = url.replace(paramStr, '_' + token + '_');
                }
            }
        }

        List<AnObject> queryParams = endpointInfo.getQueryParams();
        if (queryParams != null && !queryParams.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < queryParams.size(); i++) {
                if (i == 0) {
                    sb.append('?');
                }
                else {
                    sb.append('&');
                }
                String name = queryParams.get(i).getName();
                sb.append(name).append("=\" + ").append(name).append(" + \"");
            }
            url = url + sb.toString();
        }
        return url;
    }
}
