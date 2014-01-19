package com.zvoykish.restdl.objects;

import com.zvoykish.restdl.TypeHelper;
import com.zvoykish.restdl.objects.types.AnObject;
import com.zvoykish.restdl.objects.types.TypedObject;
import com.zvoykish.restdl.objects.types.TypedObjectWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:37
 */
@SuppressWarnings("UnusedDeclaration")
@org.codehaus.jackson.map.annotate.JsonSerialize(
        include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(
        include = com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL)
public class EndpointInfo implements Comparable<EndpointInfo> {
    private static final List<String> VOID_TYPES = new ArrayList<>();

    static {
        VOID_TYPES.add("void");
        VOID_TYPES.add("java.lang.Void");
    }

    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    private String controllerName;
    private String methodName;
    private String description;
    private HttpMethod httpMethod;
    private String url;
    private List<AnObject> pathParams;
    private List<AnObject> queryParams;
    private TypedObject requestParam;
    private TypedObject returnType;
    private String usedBy;

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<AnObject> getPathParams() {
        return pathParams;
    }

    public void setPathParams(List<AnObject> pathParams) {
        this.pathParams = pathParams;
    }

    public List<AnObject> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<AnObject> queryParams) {
        this.queryParams = queryParams;
    }

    public TypedObject getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(TypedObject requestParam) {
        this.requestParam = requestParam;
    }

    public TypedObject getReturnType() {
        return returnType;
    }

    public void setReturnType(TypedObject returnType) {
        this.returnType = returnType;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getHttpMethodDisplayName() {
        return httpMethod != null ? httpMethod.name() : HttpMethod.GET.name();
    }

    public void updateReferences(Map<Long, TypedObject> typesById) {
        updateListReferences(pathParams);
        updateListReferences(queryParams);

        if (requestParam instanceof TypedObjectWrapper) {
            requestParam = ((TypedObjectWrapper) requestParam).getInstance().get();
            if (!TypeHelper.INLINE_TYPES.get()) {
                requestParam = requestParam.toReference();
            }
        }

        if (returnType instanceof TypedObjectWrapper) {
            returnType = ((TypedObjectWrapper) returnType).getInstance().get();
            if (!TypeHelper.INLINE_TYPES.get()) {
                returnType = returnType.toReference();
            }
        }
    }

    private void updateListReferences(List<AnObject> list) {
        if (list != null) {
            for (AnObject queryParam : list) {
                TypedObject type = queryParam.getType();
                if (type instanceof TypedObjectWrapper) {
                    type = ((TypedObjectWrapper) type).getInstance().get();
                    if (!TypeHelper.INLINE_TYPES.get()) {
                        type = type.toReference();
                    }
                    queryParam.setType(type);
                }
            }
        }
    }

    public String generateEndpointName() {
        String[] tokens = url.split("/");
        if (tokens.length == 0) {
            return methodName + "Of" + controllerName;
        }

        EndpointInfo.HttpMethod method = httpMethod;
        if (method == null) {
            method = EndpointInfo.HttpMethod.GET;
        }

        StringBuilder sb = new StringBuilder();
        String methodName = method.name().toLowerCase();
        sb.append(methodName);
        for (String token : tokens) {
            token = token.trim().replaceAll("[^a-zA-Z0-9]", "");
            if (!token.isEmpty()) {
                sb.append(Character.toUpperCase(token.charAt(0)));
                if (token.length() > 1) {
                    sb.append(token.substring(1).toLowerCase());
                }
            }
        }
        return sb.toString();
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean hasReturnType() {
        return returnType != null && !VOID_TYPES.contains(returnType.getClassName());
    }

    @Override
    public int compareTo(EndpointInfo o) {
        return (getHttpMethod() + getUrl()).compareTo(o.getHttpMethod() + o.getUrl());
    }

    @Override
    public String toString() {
        return "EndpointInfo{" +
                "httpMethod=" + httpMethod +
                ", url='" + url + '\'' +
                '}';
    }
}
