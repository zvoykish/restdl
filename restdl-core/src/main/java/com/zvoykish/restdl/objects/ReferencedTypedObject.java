package com.zvoykish.restdl.objects;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 00:37
 */
@SuppressWarnings("UnusedDeclaration")
public class ReferencedTypedObject extends ClassAwareTypedObject {
    private long id;

    protected ReferencedTypedObject() {
    }

    public ReferencedTypedObject(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return TypedObjectType.Reference.name();
    }

    @Override
    public ReferencedTypedObject toReference() {
        return this;
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        throw new UnsupportedOperationException(
                "Shouldn't be invoked directly on referenced typed objects, rather - replace their references");
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        throw new UnsupportedOperationException(
                "Shouldn't be invoked directly on referenced typed objects, rather - replace their references");
    }

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public String toString() {
        return "ReferencedTypedObject{" +
                "id=" + id +
                '}';
    }
}
