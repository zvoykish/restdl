package com.zvoykish.restdl.objects;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 10:30
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class ClassAwareTypedObject implements TypedObject {
    private String objectTypeClass;

    protected ClassAwareTypedObject() {
        this.objectTypeClass = getClass().getCanonicalName();
    }

    @Override
    public String getObjectTypeClass() {
        return objectTypeClass;
    }
}
