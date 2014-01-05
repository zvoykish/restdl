package com.zvoykish.restdl.objects;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:51
 */
@SuppressWarnings("UnusedDeclaration")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class AnObject {
    private String name;
    private TypedObject type;
    private String defaultValue;

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

    public String toString() {
        return "AnObject{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }
}
