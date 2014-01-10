package com.zvoykish.restdl.objects;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 09:34
 */
@org.codehaus.jackson.map.annotate.JsonSerialize(
        include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(
        include = com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL)
public class ParametrizedComplexObject extends BaseTypedObject {
    private String type;
    private ReferencedTypedObject rawTypeReference;
    private TypedObject rawType;
    private List<TypedObject> types;

    public ParametrizedComplexObject(String type, ReferencedTypedObject rawType, List<TypedObject> types) {
        this.type = type;
        this.rawTypeReference = rawType;
        this.types = types;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void updateReferences(Map<Long, TypedObject> typesById) {
        rawType = typesById.get(rawTypeReference.getId());
        rawTypeReference = null;

        for (int i = 0; i < types.size(); i++) {
            TypedObject objectType = types.get(i);
            if (objectType instanceof ReferencedTypedObject) {
                TypedObject object = typesById.get(objectType.getId());
                types.set(i, object);
            }
        }
    }

    @Override
    public String toString() {
        return "ParametrizedComplexObject{" +
                "type='" + type + '\'' +
                ", rawType=" + rawType +
                ", types=" + types +
                '}';
    }
}
