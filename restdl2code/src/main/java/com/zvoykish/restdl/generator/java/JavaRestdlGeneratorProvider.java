package com.zvoykish.restdl.generator.java;

import com.zvoykish.restdl.generator.ContentGenerator;
import com.zvoykish.restdl.generator.HttpClientFactoryInterface;
import com.zvoykish.restdl.generator.HttpClientFactoryInterfaceImpl;
import com.zvoykish.restdl.generator.RestdlGeneratorProvider;
import com.zvoykish.restdl.generator.java.impl.ClassContentGenerator;
import com.zvoykish.restdl.generator.java.impl.EnumObjectContentGenerator;
import com.zvoykish.restdl.generator.java.impl.PrimitiveObjectContentGenerator;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;
import com.zvoykish.restdl.objects.TypedObjectType;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/12/14
 * Time: 22:50
 */
public class JavaRestdlGeneratorProvider implements RestdlGeneratorProvider {
    public static final String CLASS_NAME_REST_TEMPLATE_FACTORY = "RestdlRestTemplateFactory";
    private final JavaWriter writer;
    private final Map<TypedObjectType, ContentGenerator> contentGeneratorMap;

    public JavaRestdlGeneratorProvider() {
        writer = new JavaWriter();
        contentGeneratorMap = new HashMap<>();
        contentGeneratorMap.put(TypedObjectType.Primitive, new PrimitiveObjectContentGenerator());
        contentGeneratorMap.put(TypedObjectType.Enum, new EnumObjectContentGenerator());
        contentGeneratorMap.put(TypedObjectType.Other, new ClassContentGenerator(writer));

        java.util.Properties p = new java.util.Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
    }

    @Override
    public boolean isIgnoredType(String objectTypeClass) {
        return objectTypeClass == null || objectTypeClass.indexOf('.') == -1 ||
                objectTypeClass.indexOf('<') > -1 || objectTypeClass.indexOf('[') > -1;
    }

    @Override
    public String generateTypeContents(TypedObject type, String className, Map<Long, TypedObject> typeMap) {
        String objectTypeClass = type.getClassName();
        if (objectTypeClass == null || objectTypeClass.indexOf('.') == -1 ||
                objectTypeClass.indexOf('<') > -1 || objectTypeClass.indexOf('[') > -1)
        {
            System.out.println("Unhandled class: " + type);
            return null;
        }

        String targetPackage = getPackage(objectTypeClass);
        String contents = generateClassContents(type, targetPackage, className, typeMap);
        return contents;
    }

    @Override
    public String generateApiInterface(List<EndpointInfo> endpoints, String className, String packageName,
                                       Map<Long, TypedObject> typeMap)
    {
        Template template = Velocity.getTemplate("templates/interface.vm");
        StringWriter stringWriter = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("interfaceName", className);
        context.put("endpoints", endpoints);
        context.put("writer", writer);
        context.put("types", typeMap);
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

    @Override
    public String generateApiImplementation(List<EndpointInfo> endpoints, String className, String packageName,
                                            Map<Long, TypedObject> typeMap)
    {
        Template template = Velocity.getTemplate("templates/class.vm");
        StringWriter stringWriter = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("clientFactoryClassName", CLASS_NAME_REST_TEMPLATE_FACTORY);
        context.put("superInterfaces", new String[]{className.substring(0, className.length() - 4)});
        context.put("className", className);
        context.put("endpoints", endpoints);
        context.put("writer", writer);
        context.put("types", typeMap);
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

    @Override
    public HttpClientFactoryInterface generateHttpClientFactory(String packageName) {
        Template template = Velocity.getTemplate("templates/resttemplate_factory.vm");
        StringWriter stringWriter = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("factoryInterfaceName", CLASS_NAME_REST_TEMPLATE_FACTORY);
        template.merge(context, stringWriter);
        return new HttpClientFactoryInterfaceImpl(CLASS_NAME_REST_TEMPLATE_FACTORY, stringWriter.toString());
    }

    @Override
    public String getClassFileExtension() {
        return "java";
    }

    public String classNameToPath(String targetPackage) {
        return targetPackage.replace('.', File.separatorChar).replace('$', '_');
    }

    private String getPackage(String objectTypeClass) {
        return objectTypeClass.substring(0, objectTypeClass.lastIndexOf('.'));
    }

    private String generateClassContents(TypedObject typedObject, String targetPackage, String className,
                                         Map<Long, TypedObject> typeMap)
    {
        TypedObjectType typedObjectType = TypedObjectType.fromString(typedObject.getType());
        ContentGenerator<TypedObject> contentGenerator = contentGeneratorMap.get(typedObjectType);
        if (contentGenerator != null) {
            String content = contentGenerator.generateContent(typedObject, className, typeMap);
            if (content == null) {
                return null;
            }

            Template template = Velocity.getTemplate("templates/package_with_contents.vm");
            StringWriter stringWriter = new StringWriter();
            VelocityContext context = new VelocityContext();
            context.put("packageName", targetPackage);
            context.put("contents", content);
            template.merge(context, stringWriter);
            return stringWriter.toString();
        }
        else {
            return null;
        }
    }
}
