package com.zvoykish.restdl.objects;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:51
 */
@SuppressWarnings("UnusedDeclaration")
public class PrimitiveObject implements TypedObject {
    private String className;

    public PrimitiveObject(String className) {
        this.className = className;
    }

    @Override
    public String getType() {
        return "Primitive";
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "Primitive type: " + className;
    }
}

