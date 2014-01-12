package com.zvoykish.restdl.generator.java;

import com.zvoykish.restdl.generator.ContentGenerator;
import com.zvoykish.restdl.generator.RestdlGeneratorProvider;
import com.zvoykish.restdl.generator.java.impl.ClassContentGenerator;
import com.zvoykish.restdl.generator.java.impl.EnumObjectContentGenerator;
import com.zvoykish.restdl.generator.java.impl.PrimitiveObjectContentGenerator;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;
import com.zvoykish.restdl.objects.TypedObjectType;

import java.io.File;
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
    private final JavaWriter writer;
    private final Map<TypedObjectType, ContentGenerator> contentGeneratorMap;

    public JavaRestdlGeneratorProvider() {
        writer = new JavaWriter();
        contentGeneratorMap = new HashMap<>();
        contentGeneratorMap.put(TypedObjectType.Primitive, new PrimitiveObjectContentGenerator());
        contentGeneratorMap.put(TypedObjectType.Enum, new EnumObjectContentGenerator());
        contentGeneratorMap.put(TypedObjectType.Other, new ClassContentGenerator(writer));
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
        StringBuilder sb = new StringBuilder();

        writer.writePackage(sb, packageName);
        sb.append("public interface ").append(className).append(" {").append(ContentGenerator.EOL);
        for (EndpointInfo endpoint : endpoints) {
            sb.append(ContentGenerator.EOL);
            sb.append('\t');
            writer.writeSignatureClass(sb, endpoint.getReturnType(), typeMap);
            sb.append(' ');
            writer.writeMethodName(sb, endpoint);
            sb.append('(');
            sb.append(')');
            sb.append(ContentGenerator.EOL_CODE);
        }
        sb.append('}').append(ContentGenerator.EOL);

        return sb.toString();
    }

    @Override
    public String generateApiImplementation(List<EndpointInfo> endpoints, String className, String packageName,
                                            Map<Long, TypedObject> typeMap)
    {
        StringBuilder sb = new StringBuilder();

        writer.writePackage(sb, packageName);
        String interfaceName = className.substring(0, className.length() - 4);
        sb.append("public class ").append(className).append(" implements ").append(interfaceName).append(" {")
                .append(ContentGenerator.EOL);
        for (EndpointInfo endpoint : endpoints) {
            sb.append(ContentGenerator.EOL);
            sb.append('\t');
            sb.append("public ");
            writer.writeSignatureClass(sb, endpoint.getReturnType(), typeMap);
            sb.append(' ');
            writer.writeMethodName(sb, endpoint);
            sb.append('(');
            sb.append(')');
            sb.append("{}");
            sb.append(ContentGenerator.EOL);
        }
        sb.append('}').append(ContentGenerator.EOL);

        return sb.toString();
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
            StringBuilder sb = new StringBuilder();
            writer.writePackage(sb, targetPackage);
            String content = contentGenerator.generateContent(typedObject, className, typeMap);
            if (content == null) {
                return null;
            }

            sb.append(content);
            return sb.toString();
        }
        else {
            return null;
        }
    }
}
