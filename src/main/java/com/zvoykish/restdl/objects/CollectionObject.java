package com.zvoykish.restdl.objects;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:54
 */
@SuppressWarnings("UnusedDeclaration")
public class CollectionObject implements TypedObject {
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
    public String toString() {
        return "Collection of type: " + elementType;
    }
}
