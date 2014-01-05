package com.zvoykish.restdl.objects;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:51
 */
@SuppressWarnings("UnusedDeclaration")
public class ComplexObject implements TypedObject {
    private String type;
    private List<AnObject> fields;

    public ComplexObject(String type, List<AnObject> fields) {
        this.type = type;
        this.fields = fields;
    }

    @Override
    public String getType() {
        return type;
    }

    public List<AnObject> getFields() {
        return fields;
    }

    public String toString() {
        return "ComplexObject{" +
                "type='" + type + '\'' +
                ", fields=" + fields +
                '}';
    }
}
