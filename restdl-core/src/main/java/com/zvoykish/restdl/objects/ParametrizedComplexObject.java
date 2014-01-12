package com.zvoykish.restdl.objects;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 09:34
 */
@SuppressWarnings("UnusedDeclaration")
@org.codehaus.jackson.map.annotate.JsonSerialize(
        include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(
        include = com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL)
public class ParametrizedComplexObject extends BaseTypedObject {
    private String type;
    private TypedObject rawTypeReference;
    private TypedObject rawType;
    private List<TypedObject> types;

    protected ParametrizedComplexObject() {
    }

    public ParametrizedComplexObject(String type, TypedObject rawType, List<TypedObject> types) {
        this.type = type;
        this.rawTypeReference = rawType;
        this.types = types;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TypedObject getRawTypeReference() {
        return rawTypeReference;
    }

    public void setRawTypeReference(TypedObject rawTypeReference) {
        this.rawTypeReference = rawTypeReference;
    }

    public TypedObject getRawType() {
        return rawType;
    }

    public void setRawType(TypedObject rawType) {
        this.rawType = rawType;
    }

    public List<TypedObject> getTypes() {
        return types;
    }

    public void setTypes(List<TypedObject> types) {
        this.types = types;
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        if (rawTypeReference instanceof TypedObjectWrapper) {
            rawTypeReference = ((TypedObjectWrapper) rawTypeReference).getInstance().get().toReference();
        }
        rawType = rawTypeReference;
        rawTypeReference = null;

        for (int i = 0; i < types.size(); i++) {
            TypedObject objectType = types.get(i);
            if (objectType instanceof TypedObjectWrapper) {
                objectType = ((TypedObjectWrapper) objectType).getInstance().get().toReference();
                types.set(i, objectType);
            }
        }
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        if (rawType instanceof ReferencedTypedObject) {
            rawType = typesById.get(rawType.getId());
        }
        for (int i = 0; types != null && i < types.size(); i++) {
            TypedObject objectType = types.get(i);
            if (objectType instanceof ReferencedTypedObject) {
                objectType = typesById.get(objectType.getId());
                types.set(i, objectType);
            }
        }
    }

    @Override
    public String getClassName() {
        return type;
    }

    @Override
    public String toString() {
        return "ParametrizedComplexObject{" +
                "type='" + type + '\'' +
                ", rawTypeReference=" + rawTypeReference +
                ", rawType=" + rawType +
                ", types=" + types +
                "} " + super.toString();
    }
}
