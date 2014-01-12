package com.zvoykish.restdl.objects;

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
    private TypedObject keys;
    private TypedObject values;

    protected MapObject() {
    }

    public MapObject(String className, TypedObject keys, TypedObject values) {
        this.className = className;
        this.type = "Map";
        this.keys = keys;
        this.values = values;
    }

    @Override
    public String getType() {
        return type;
    }

    public TypedObject getKeys() {
        return keys;
    }

    public TypedObject getValues() {
        return values;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKeys(TypedObject keys) {
        this.keys = keys;
    }

    public void setValues(TypedObject values) {
        this.values = values;
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        if (keys instanceof TypedObjectWrapper) {
            keys = ((TypedObjectWrapper) keys).getInstance().get().toReference();
        }

        if (values instanceof TypedObjectWrapper) {
            values = ((TypedObjectWrapper) values).getInstance().get().toReference();
        }
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        if (keys instanceof ReferencedTypedObject) {
            keys = typesById.get(keys.getId());
        }
        if (values instanceof ReferencedTypedObject) {
            values = typesById.get(values.getId());
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
                ", keys=" + keys +
                ", values=" + values +
                "} " + super.toString();
    }
}
