package com.zvoykish.restdl.objects.types;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 21:55
 */
@SuppressWarnings("UnusedDeclaration")
public class GenericDeclarationObject extends BaseTypedObject {
    private String className;

    public GenericDeclarationObject() {
    }

    public GenericDeclarationObject(String className) {
        this.className = className;
    }

    @Override
    public String getType() {
        return TypedObjectType.GenericDeclaration.name();
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        // Nothing to do on such objects...
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        // Nothing to do on such objects...
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
        return "GenericDeclarationObject{" +
                "className='" + className + '\'' +
                "} " + super.toString();
    }
}
