package com.zvoykish.restdl.objects;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:50
 */
@SuppressWarnings("UnusedDeclaration")
public interface TypedObject {
    long getId();

    String getType();

    ReferencedTypedObject toReference();

    void updateReferences(Map<Long, TypedObject> typesById);
}
