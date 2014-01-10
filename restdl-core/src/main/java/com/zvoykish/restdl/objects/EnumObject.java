package com.zvoykish.restdl.objects;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/9/14
 * Time: 23:26
 */
@SuppressWarnings("UnusedDeclaration")
public class EnumObject extends BaseTypedObject {
    private String type;
    private String className;
    private List<String> constants;

    public EnumObject(String className, List<String> constants) {
        this.type = "Enum";
        this.className = className;
        this.constants = constants;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void updateReferences(Map<Long, TypedObject> typesById) {
        // No objects to update
    }

    public String getClassName() {
        return className;
    }

    public List<String> getConstants() {
        return constants;
    }

    @Override
    public String toString() {
        return "EnumObject{" +
                "type='" + type + '\'' +
                ", className='" + className + '\'' +
                ", constants=" + constants +
                '}';
    }
}
