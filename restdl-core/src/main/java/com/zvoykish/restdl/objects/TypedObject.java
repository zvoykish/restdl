package com.zvoykish.restdl.objects;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:50
 */
@SuppressWarnings("UnusedDeclaration")
@org.codehaus.jackson.map.annotate.JsonDeserialize(using = TypedObjectCodehausDeserializer.class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = TypedObjectFasterXmlDeserializer.class)
public interface TypedObject {
    long getId();

    String getType();

    ReferencedTypedObject toReference();

    void referenceFields(Map<Long, TypedObject> typesById);

    void unReferenceFields(Map<Long, TypedObject> typesById);

    String getClassName();

    String getObjectTypeClass();
}
