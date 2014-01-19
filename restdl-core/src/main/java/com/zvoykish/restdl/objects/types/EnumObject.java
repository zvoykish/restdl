package com.zvoykish.restdl.objects.types;

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

    public EnumObject() {
    }

    public EnumObject(String className, List<String> constants) {
        this.type = TypedObjectType.Enum.name();
        this.className = className;
        this.constants = constants;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        // No objects to update
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        // No objects to update
    }

    @Override
    public String getClassName() {
        return className;
    }

    public List<String> getConstants() {
        return constants;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setConstants(List<String> constants) {
        this.constants = constants;
    }

    @Override
    public String toString() {
        return "EnumObject{" +
                "type='" + type + '\'' +
                ", className='" + className + '\'' +
                ", constants=" + constants +
                "} " + super.toString();
    }
}
