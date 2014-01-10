package com.zvoykish.restdl.objects;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 00:37
 */
public class ReferencedTypedObject implements TypedObject {
    private long id;

    public ReferencedTypedObject(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getType() {
        return "Reference";
    }

    @Override
    public ReferencedTypedObject toReference() {
        return this;
    }

    @Override
    public void updateReferences(Map<Long, TypedObject> typesById) {
        throw new UnsupportedOperationException(
                "Shouldn't be invoked directly on referenced typed objects, rather - replace their references");
    }

    @Override
    public String toString() {
        return "ReferencedTypedObject{" +
                "id=" + id +
                '}';
    }
}
