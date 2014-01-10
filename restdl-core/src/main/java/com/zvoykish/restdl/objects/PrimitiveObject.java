package com.zvoykish.restdl.objects;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:51
 */
@SuppressWarnings("UnusedDeclaration")
public class PrimitiveObject extends BaseTypedObject {
    private String className;

    public PrimitiveObject(String className) {
        this.className = className;
    }

    @Override
    public String getType() {
        return "Primitive";
    }

    @Override
    public void updateReferences(Map<Long, TypedObject> typesById) {
        // No objects to update
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "Primitive: " + className;
    }
}

