package com.zvoykish.restdl.objects;

import com.zvoykish.restdl.TypeHelper;

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
    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    private String description;
    private HttpMethod httpMethod;
    private String url;
    private List<AnObject> queryParams;
    private TypedObject requestParam;
    private TypedObject returnType;
    private String usedBy;

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

    public void updateReferences(Map<Long, TypedObject> typesById) {
        if (queryParams != null) {
            for (AnObject queryParam : queryParams) {
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

    @Override
    public int compareTo(EndpointInfo o) {
        return (getHttpMethod() + getUrl()).compareTo(o.getHttpMethod() + o.getUrl());
    }
}
