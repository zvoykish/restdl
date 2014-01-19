package com.zvoykish.restdl.objects.types;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/6/13
 * Time: 22:59
 */
@SuppressWarnings("UnusedDeclaration")
public class MapObject extends BaseTypedObject {
    private String type;
    private String className;
    private TypedObject keyType;
    private TypedObject valueType;

    public MapObject() {
    }

    public MapObject(String className, TypedObject keyType, TypedObject valueType) {
        this.className = className;
        this.type = "Map";
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public String getType() {
        return type;
    }

    public TypedObject getKeyType() {
        return keyType;
    }

    public TypedObject getValueType() {
        return valueType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKeyType(TypedObject keyType) {
        this.keyType = keyType;
    }

    public void setValueType(TypedObject valueType) {
        this.valueType = valueType;
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        if (keyType instanceof TypedObjectWrapper) {
            keyType = ((TypedObjectWrapper) keyType).getInstance().get().toReference();
        }

        if (valueType instanceof TypedObjectWrapper) {
            valueType = ((TypedObjectWrapper) valueType).getInstance().get().toReference();
        }
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        if (keyType instanceof ReferencedTypedObject) {
            keyType = typesById.get(keyType.getId());
        }
        if (valueType instanceof ReferencedTypedObject) {
            valueType = typesById.get(valueType.getId());
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
        return "MapObject{" +
                "type='" + type + '\'' +
                ", keys=" + keyType +
                ", values=" + valueType +
                "} " + super.toString();
    }
}
