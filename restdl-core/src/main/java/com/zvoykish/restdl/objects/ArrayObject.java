package com.zvoykish.restdl.objects;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/12/14
 * Time: 20:29
 */
@SuppressWarnings("UnusedDeclaration")
public class ArrayObject extends BaseTypedObject {
    private String className;
    private TypedObject component;

    protected ArrayObject() {
    }

    public ArrayObject(String className, TypedObject component) {
        this.className = className;
        this.component = component;
    }

    @Override
    public String getType() {
        return TypedObjectType.Array.name();
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        if (component instanceof TypedObjectWrapper) {
            component = ((TypedObjectWrapper) component).getInstance().get().toReference();
        }
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        if (component instanceof ReferencedTypedObject) {
            component = typesById.get(component.getId());
        }
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public TypedObject getComponent() {
        return component;
    }

    public void setComponent(TypedObject component) {
        this.component = component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArrayObject that = (ArrayObject) o;

        if (className != null ? !className.equals(that.className) : that.className != null) {
            return false;
        }
        if (component != null ? !component.equals(that.component) : that.component != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (component != null ? component.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArrayObject{" +
                "className='" + className + '\'' +
                ", component=" + component +
                "} " + super.toString();
    }
}
