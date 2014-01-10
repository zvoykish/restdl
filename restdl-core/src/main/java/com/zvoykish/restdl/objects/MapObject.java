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
    private TypedObject keys;
    private TypedObject values;

    public MapObject(TypedObject keys, TypedObject values) {
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

    @Override
    public void updateReferences(Map<Long, TypedObject> typesById) {
        if (keys instanceof ReferencedTypedObject) {
            keys = typesById.get(keys.getId());
        }

        if (values instanceof ReferencedTypedObject) {
            values = typesById.get(values.getId());
        }
    }

    public String toString() {
        return "Map{" +
                ", keys=" + keys +
                ", values=" + values +
                '}';
    }
}
