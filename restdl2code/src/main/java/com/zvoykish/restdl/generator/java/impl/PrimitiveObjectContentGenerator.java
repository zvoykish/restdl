package com.zvoykish.restdl.generator.java.impl;

import com.zvoykish.restdl.generator.ContentGenerator;
import com.zvoykish.restdl.objects.PrimitiveObject;
import com.zvoykish.restdl.objects.TypedObject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/11/14
 * Time: 19:44
 */
public class PrimitiveObjectContentGenerator implements ContentGenerator<PrimitiveObject> {
    private static final List<Pattern> IGNORED_CLASSES = new ArrayList<>();

    static {
        IGNORED_CLASSES.add(Pattern.compile("(void)|(long)|(int)(boolean)|(float)|(double)|(short)|(byte)"));
        IGNORED_CLASSES.add(Pattern.compile("java..*"));
    }

    @Override
    public String generateContent(PrimitiveObject object, String className, Map<Long, TypedObject> typeMap) {
        for (Pattern ignoredClass : IGNORED_CLASSES) {
            if (ignoredClass.matcher(object.getClassName()).find()) {
                return null;
            }
        }

        Template template = Velocity.getTemplate("templates/object_primitive.vm");
        StringWriter stringWriter = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("className", className);
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }
}
