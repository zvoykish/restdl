package com.zvoykish.restdl.objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zvoykish.restdl.objects.types.TypedObject;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private String md5;
    private long timestamp;
    private String timestampString;

    protected ApiDetailsResponse() {
    }

    public ApiDetailsResponse(List<EndpointInfo> endpoints, List<TypedObject> types) {
        this.endpoints = endpoints;
        this.types = types;
        this.md5 = null;

        Date now = new Date();
        this.timestamp = now.getTime();
        this.timestampString = new SimpleDateFormat().format(now);
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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestampString() {
        return timestampString;
    }

    public void setTimestampString(String timestampString) {
        this.timestampString = timestampString;
    }

    public void calculateMd5() throws Exception {
        Collections.sort(endpoints, new Comparator<EndpointInfo>() {
            @Override
            public int compare(EndpointInfo o1, EndpointInfo o2) {
                return o1.getUrl().compareTo(o2.getUrl());
            }
        });

        Collections.sort(types, new Comparator<TypedObject>() {
            @Override
            public int compare(TypedObject o1, TypedObject o2) {
                return o1.getClassName().compareTo(o2.getClassName());
            }
        });

        JsonNode node = new ObjectMapper().valueToTree(new Object[]{endpoints, types});
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md.digest(node.toString().getBytes("UTF-8"));
        this.md5 = new BASE64Encoder().encode(md5Bytes);
    }

    @Override
    public String toString() {
        return "ApiDetailsResponse{" +
                "endpoints=" + endpoints +
                ", types=" + types +
                ", md5='" + md5 + '\'' +
                ", timestamp=" + timestamp +
                ", timestampString='" + timestampString + '\'' +
                '}';
    }
}
