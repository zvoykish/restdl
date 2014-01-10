package com.zvoykish.restdl.objects;

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
    private TypedObject elementType;

    public CollectionObject(TypedObject elementType) {
        this.type = "Collection";
        this.elementType = elementType;
    }

    @Override
    public String getType() {
        return type;
    }

    public TypedObject getElementType() {
        return elementType;
    }

    @Override
    public void updateReferences(Map<Long, TypedObject> typesById) {
        if (elementType instanceof ReferencedTypedObject) {
            elementType = typesById.get(elementType.getId());
        }
    }

    @Override
    public String toString() {
        return "Collection: " + elementType;
    }
}
