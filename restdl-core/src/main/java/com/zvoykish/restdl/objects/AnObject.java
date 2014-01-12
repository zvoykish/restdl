package com.zvoykish.restdl.objects;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:51
 */
@SuppressWarnings("UnusedDeclaration")
@org.codehaus.jackson.map.annotate.JsonSerialize(
        include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(
        include = com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL)
public class AnObject {
    private String name;
    private TypedObject type;
    private String defaultValue;

    protected AnObject() {
    }

    public AnObject(String name, TypedObject type) {
        this(name, type, null);
    }

    public AnObject(String name, TypedObject type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public TypedObject getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypedObject type) {
        this.type = type;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String toString() {
        return "AnObject{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }
}
