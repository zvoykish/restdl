package com.zvoykish.restdl.java;

import com.zvoykish.restdl.java.generators.ContentGenerator;
import com.zvoykish.restdl.objects.CollectionObject;
import com.zvoykish.restdl.objects.EndpointInfo;
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
    public static void writePackage(StringBuilder sb, String packageName) {
        sb.append("package ").append(packageName).append(ContentGenerator.EOL_CODE).append(ContentGenerator.EOL);
    }

    public static void writeClassStart(StringBuilder sb, String className) {
        sb.append("public class ").append(className).append(" {").append(ContentGenerator.EOL);
    }

    public static void writeSignatureClass(StringBuilder sb, TypedObject type, Map<Long, TypedObject> typeMap) {
        sb.append(resolveTypeName(type, typeMap));
    }

    private static String resolveTypeName(TypedObject type, Map<Long, TypedObject> typeMap) {
        if (type instanceof CollectionObject) {
            TypedObject elementType = ((CollectionObject) type).getElementType();
            String elementClassName = typeMap.get(elementType.getId()).getClassName();
            return type.getClassName() + '<' + elementClassName + '>';
        }
        else if (type instanceof MapObject) {
            TypedObject keyType = ((MapObject) type).getKeyType();
            TypedObject valueType = ((MapObject) type).getValueType();
            String keyClassName = typeMap.get(keyType.getId()).getClassName();
            String valueClassName = typeMap.get(valueType.getId()).getClassName();
            return type.getClassName() + '<' + keyClassName + ", " + valueClassName + '>';
        }

        return type.getClassName().replace('$', '_');
    }

    public static void writeMethodName(StringBuilder sb, EndpointInfo endpoint) {
        EndpointInfo.HttpMethod httpMethod = endpoint.getHttpMethod();
        if (httpMethod == null) {
            httpMethod = EndpointInfo.HttpMethod.GET;
        }

        String methodName = httpMethod.name().toLowerCase();
        String url = endpoint.getUrl();
        String[] tokens = url.split("/");
        sb.append(methodName);
        for (String token : tokens) {
            if (!token.startsWith("{")) {
                token = token.trim().replaceAll("[^a-zA-Z]", "");
                if (!token.isEmpty()) {
                    sb.append(Character.toUpperCase(token.charAt(0)));
                    if (token.length() > 1) {
                        sb.append(token.substring(1).toLowerCase());
                    }
                }
            }
        }
    }
}
