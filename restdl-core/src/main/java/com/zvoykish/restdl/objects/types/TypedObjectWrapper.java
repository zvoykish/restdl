package com.zvoykish.restdl.objects.types;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/12/14
 * Time: 02:38
 */
public class TypedObjectWrapper implements TypedObject {
    private AtomicReference<TypedObject> instance;

    public TypedObjectWrapper(AtomicReference<TypedObject> instance) {
        this.instance = instance;
    }

    public AtomicReference<TypedObject> getInstance() {
        return instance;
    }

    @Override
    public long getId() {
        return instance.get().getId();
    }

    @Override
    public String getType() {
        return instance.get().getType();
    }

    @Override
    public ReferencedTypedObject toReference() {
        ReferencedTypedObject referencedTypedObject = instance.get().toReference();
        if (referencedTypedObject == null) {
            throw new RuntimeException("No good!!!");
        }
        return referencedTypedObject;
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        throw new UnsupportedOperationException("No good...");
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        throw new UnsupportedOperationException("No good...");
    }

    @Override
    public String getClassName() {
        return instance.get().getClassName();
    }

    @Override
    public String getObjectTypeClass() {
        return instance.get().getObjectTypeClass();
    }

    @Override
    public String toString() {
        return "TypedObjectWrapper{" +
                "instance=" + instance +
                '}';
    }
}
