package com.zvoykish.restdl.generator.java.impl;

import com.zvoykish.restdl.generator.ContentGenerator;
import com.zvoykish.restdl.generator.java.JavaWriter;
import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.ComplexObject;
import com.zvoykish.restdl.objects.GenericDeclarationObject;
import com.zvoykish.restdl.objects.TypedObject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 20:31
 */
public class ComplexObjectGenerator implements ContentGenerator<ComplexObject> {
    private final JavaWriter writer;

    public ComplexObjectGenerator(JavaWriter writer) {
        this.writer = writer;
    }

    @Override
    public String generateContent(ComplexObject object, String className, Map<Long, TypedObject> typeMap) {
        Template template = Velocity.getTemplate("templates/object_class.vm");
        StringWriter stringWriter = new StringWriter();
        VelocityContext context = new VelocityContext();
        List<AnObject> fields = object.getFields();
        context.put("className", getClassName(className, fields));
        context.put("fields", fields);
        context.put("writer", writer);
        context.put("types", typeMap);
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

    private String getClassName(String className, List<AnObject> fields) {
        List<String> genericObjects = new ArrayList<>();
        for (AnObject field : fields) {
            TypedObject fieldType = field.getType();
            if (fieldType instanceof GenericDeclarationObject) {
                genericObjects.add(fieldType.getClassName());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(className);
        if (!genericObjects.isEmpty()) {
            sb.append('<');
            for (int i = 0; i < genericObjects.size(); i++) {
                sb.append(genericObjects.get(i));
                if (i < genericObjects.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append('>');
        }
        return sb.toString();
    }

}
