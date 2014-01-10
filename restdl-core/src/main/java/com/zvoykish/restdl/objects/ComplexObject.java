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

    @Override
    public void updateReferences(Map<Long, TypedObject> typesById) {
        for (AnObject field : fields) {
            TypedObject objectType = field.getType();
            if (objectType instanceof ReferencedTypedObject) {
                TypedObject object = typesById.get(objectType.getId());
                field.setType(object);
            }
        }
    }

    public String toString() {
        return "Object{" +
                "type='" + type + '\'' +
                ", fields=" + fields +
                '}';
    }
}
