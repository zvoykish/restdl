package com.zvoykish.restdl.objects.types;

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

    public PrimitiveObject() {
    }

    public PrimitiveObject(String className) {
        this.className = className;
    }

    @Override
    public String getType() {
        return TypedObjectType.Primitive.name();
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        // No objects to update
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        // No objects to update
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "PrimitiveObject{" +
                "className='" + className + '\'' +
                "} " + super.toString();
    }
}

