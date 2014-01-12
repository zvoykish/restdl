package com.zvoykish.restdl.generator.java;

import com.zvoykish.restdl.generator.ContentGenerator;
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
    public void writePackage(StringBuilder sb, String packageName) {
        sb.append("package ").append(packageName).append(ContentGenerator.EOL_CODE).append(ContentGenerator.EOL);
    }

    public void writeClassStart(StringBuilder sb, String className) {
        sb.append("public class ").append(className).append(" {").append(ContentGenerator.EOL);
    }

    public void writeSignatureClass(StringBuilder sb, TypedObject type, Map<Long, TypedObject> typeMap) {
        sb.append(resolveTypeName(type, typeMap));
    }

    public void writeMethodName(StringBuilder sb, EndpointInfo endpoint) {
        String url = endpoint.getUrl();
        String[] tokens = url.split("/");
        if (tokens.length == 0) {
            String controllerName = endpoint.getControllerName();
            String methodName = endpoint.getMethodName();
            sb.append(methodName).append("Of").append(controllerName);
            return;
        }

        EndpointInfo.HttpMethod httpMethod = endpoint.getHttpMethod();
        if (httpMethod == null) {
            httpMethod = EndpointInfo.HttpMethod.GET;
        }

        String methodName = httpMethod.name().toLowerCase();
        sb.append(methodName);
        for (String token : tokens) {
            token = token.trim().replaceAll("[^a-zA-Z]", "");
            if (!token.isEmpty()) {
                sb.append(Character.toUpperCase(token.charAt(0)));
                if (token.length() > 1) {
                    sb.append(token.substring(1).toLowerCase());
                }
            }
        }
    }

    private String resolveTypeName(TypedObject type, Map<Long, TypedObject> typeMap) {
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
