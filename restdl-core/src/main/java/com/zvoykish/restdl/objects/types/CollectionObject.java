package com.zvoykish.restdl.objects.types;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:54
 */
@SuppressWarnings("UnusedDeclaration")
public class CollectionObject extends BaseTypedObject {
    private String type;
    private String className;
    private TypedObject elementType;

    public CollectionObject() {
    }

    public CollectionObject(String className, TypedObject elementType) {
        this.className = className;
        this.type = TypedObjectType.Collection.name();
        this.elementType = elementType;
    }

    @Override
    public String getType() {
        return type;
    }

    public TypedObject getElementType() {
        return elementType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setElementType(TypedObject elementType) {
        this.elementType = elementType;
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        if (elementType instanceof TypedObjectWrapper) {
            elementType = ((TypedObjectWrapper) elementType).getInstance().get().toReference();
        }
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        if (elementType instanceof ReferencedTypedObject) {
            elementType = typesById.get(elementType.getId());
        }
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
        return "CollectionObject{" +
                "type='" + type + '\'' +
                ", elementType=" + elementType +
                "} " + super.toString();
    }
}
