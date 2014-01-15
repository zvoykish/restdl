package com.zvoykish.restdl.objects;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 00:51
 */
@SuppressWarnings("UnusedDeclaration")
public class ComplexObject extends BaseTypedObject {
    private String type;
    private List<AnObject> fields;

    protected ComplexObject() {
    }

    public ComplexObject(String type, List<AnObject> fields) {
        this.type = type;
        this.fields = fields;
    }

    @Override
    public String getType() {
        return type;
    }

    public List<AnObject> getFields() {
        return fields;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFields(List<AnObject> fields) {
        this.fields = fields;
    }

    @Override
    public void referenceFields(Map<Long, TypedObject> typesById) {
        for (AnObject field : fields) {
            TypedObject objectType = field.getType();
            if (objectType instanceof TypedObjectWrapper) {
                objectType = ((TypedObjectWrapper) objectType).getInstance().get().toReference();
                field.setType(objectType);
            }
        }
    }

    @Override
    public void unReferenceFields(Map<Long, TypedObject> typesById) {
        for (AnObject field : fields) {
            TypedObject objectType = field.getType();
            if (objectType instanceof ReferencedTypedObject) {
                objectType = typesById.get(objectType.getId());
                field.setType(objectType);
            }
        }
    }

    @Override
    public String getClassName() {
        return type;
    }

    @Override
    public String toString() {
        return "ComplexObject{" +
                "type='" + type + '\'' +
                "} " + super.toString();
    }
}
