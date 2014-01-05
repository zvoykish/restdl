package com.zvoykish.restdl.objects;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:37
 */
@SuppressWarnings("UnusedDeclaration")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EndpointInfo implements Comparable<EndpointInfo> {
    private String description;
    private String method;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    @Override
    public int compareTo(EndpointInfo o) {
        return (getMethod() + getUrl()).compareTo(o.getMethod() + o.getUrl());
    }
}
