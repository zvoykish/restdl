package com.zvoykish.restdl.objects;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/6/13
 * Time: 22:59
 */
@SuppressWarnings("UnusedDeclaration")
public class MapObject implements TypedObject {
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

    public String toString() {
        return "MapObject{" +
                "type='" + type + '\'' +
                ", keys=" + keys +
                ", values=" + values +
                '}';
    }
}
