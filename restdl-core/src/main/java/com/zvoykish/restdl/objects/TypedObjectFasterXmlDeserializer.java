package com.zvoykish.restdl.objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.zvoykish.restdl.objects.types.AnObject;
import com.zvoykish.restdl.objects.types.TypedObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 13:58
 */
public class TypedObjectFasterXmlDeserializer extends JsonDeserializer<TypedObject> {

    public static final String OBJECT_TYPE_CLASS = "objectTypeClass";

    @Override
    public TypedObject deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException
    {
        try {
            JsonNode jsonNode = jp.readValueAsTree();
            return deserializeObject(jsonNode);
        }
        catch (Exception e) {
            throw new JsonMappingException("Invalid object class", e);
        }
    }

    private TypedObject deserializeObject(JsonNode jsonNode)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException,
            IOException
    {
        String objectTypeClass = jsonNode.get(OBJECT_TYPE_CLASS).textValue();
        Class<? extends TypedObject> objectClass = (Class<? extends TypedObject>) Class.forName(objectTypeClass);
        TypedObject typedObject = objectClass.newInstance();
        Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> entry = it.next();
            String fieldName = entry.getKey();
            Field field = getField(typedObject.getClass(), fieldName);
            if (field != null) {
                JsonNode valueNode = entry.getValue();
                if (valueNode.isTextual()) {
                    field.set(typedObject, valueNode.textValue());
                }
                else if (valueNode.isBoolean()) {
                    field.setBoolean(typedObject, valueNode.booleanValue());
                }
                else if (valueNode.isInt()) {
                    field.setInt(typedObject, valueNode.intValue());
                }
                else if (valueNode.isLong()) {
                    field.setLong(typedObject, valueNode.longValue());
                }
                else if (valueNode.isFloat()) {
                    field.setFloat(typedObject, valueNode.floatValue());
                }
                else if (valueNode.isDouble()) {
                    field.setDouble(typedObject, valueNode.doubleValue());
                }
                else if (valueNode.isObject()) {
                    TypedObject value = deserializeObject(valueNode);
                    field.set(typedObject, value);
                }
                else if (valueNode.isArray()) {
                    List<Object> values = new ArrayList<>();
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (fieldName.equals("fields")) {
                        for (JsonNode node : valueNode) {
                            values.add(objectMapper.readValue(node.toString(), AnObject.class));
                        }
                    }
                    else {
                        for (JsonNode node : valueNode) {
                            if (node.isTextual()) {
                                values.add(node.textValue());
                            }
                            else if (node.isObject()) {
                                TypedObject object = objectMapper.readValue(node.toString(), TypedObject.class);
                                values.add(object);
                            }
                        }
                    }
                    field.set(typedObject, values);
                }
            }
        }

        return typedObject;
    }

    private Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        if (clazz == null) {
            return null;
        }
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        }
        catch (Exception e) {
            return getField(clazz.getSuperclass(), fieldName);
        }
    }
}
