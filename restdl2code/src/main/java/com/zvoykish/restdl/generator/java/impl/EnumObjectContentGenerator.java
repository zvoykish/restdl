package com.zvoykish.restdl.generator.java.impl;

import com.zvoykish.restdl.generator.ContentGenerator;
import com.zvoykish.restdl.objects.types.EnumObject;
import com.zvoykish.restdl.objects.types.TypedObject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 19:10
 */
public class EnumObjectContentGenerator implements ContentGenerator<EnumObject> {

    @Override
    public String generateContent(EnumObject object, String className, Map<Long, TypedObject> typeMap) {
        Template template = Velocity.getTemplate("templates/object_enum.vm");
        StringWriter stringWriter = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("className", className);
        context.put("constants", object.getConstants());
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }
}
