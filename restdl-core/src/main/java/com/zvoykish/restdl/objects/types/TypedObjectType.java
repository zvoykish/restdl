package com.zvoykish.restdl.objects.types;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 19:16
 */
public enum TypedObjectType {
    Reference,
    Primitive,
    Array,
    Map,
    Enum,
    Collection,
    GenericDeclaration,
    Other;

    public static TypedObjectType fromString(String s) {
        for (TypedObjectType typedObjectType : values()) {
            if (typedObjectType.name().equals(s)) {
                return typedObjectType;
            }
        }

        return Other;
    }
}
