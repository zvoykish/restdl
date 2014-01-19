package com.zvoykish.restdl.objects;

import com.zvoykish.restdl.objects.types.TypedObject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 13:56
 */
public class TypedObjectCodehausDeserializer extends JsonDeserializer<TypedObject> {
    @Override
    public TypedObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException
    {
        JsonNode jsonNode = jsonParser.readValueAsTree();
        System.out.println(jsonNode);
        return null;
    }
}
