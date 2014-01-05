package com.zvoykish.restdl.objects;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:51
 */
public class PrimitiveObject implements TypedObject {
    private String type;

    public PrimitiveObject(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Primitive type: " + type;
    }
}

