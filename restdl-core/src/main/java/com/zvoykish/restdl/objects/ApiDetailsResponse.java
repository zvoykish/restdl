package com.zvoykish.restdl.objects;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 00:18
 */
@SuppressWarnings("UnusedDeclaration")
@org.codehaus.jackson.map.annotate.JsonSerialize(
        include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(
        include = com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL)
public class ApiDetailsResponse {
    private List<EndpointInfo> endpoints;
    private List<TypedObject> types;

    public ApiDetailsResponse(List<EndpointInfo> endpoints, List<TypedObject> types) {
        this.endpoints = endpoints;
        this.types = types;
    }

    public List<EndpointInfo> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<EndpointInfo> endpoints) {
        this.endpoints = endpoints;
    }

    public List<TypedObject> getTypes() {
        return types;
    }

    public void setTypes(List<TypedObject> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "ApiDetailsResponse{" +
                "endpoints=" + endpoints +
                ", types=" + types +
                '}';
    }
}
