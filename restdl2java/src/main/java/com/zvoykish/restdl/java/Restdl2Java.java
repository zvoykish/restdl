package com.zvoykish.restdl.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zvoykish.restdl.java.generators.ClassContentGenerator;
import com.zvoykish.restdl.java.generators.ContentGenerator;
import com.zvoykish.restdl.java.generators.EnumObjectContentGenerator;
import com.zvoykish.restdl.java.generators.PrimitiveObjectContentGenerator;
import com.zvoykish.restdl.objects.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 09:55
 */
public class Restdl2Java {
    private final Map<TypedObjectType, ContentGenerator> contentGenerators = new HashMap<>();

    public static void main(String[] args) {
        try {
            new Restdl2Java().run(args[0], args[1]);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(99);
        }
    }

    public Restdl2Java() {
        contentGenerators.put(TypedObjectType.Primitive, new PrimitiveObjectContentGenerator());
        contentGenerators.put(TypedObjectType.Enum, new EnumObjectContentGenerator());
        contentGenerators.put(TypedObjectType.Other, new ClassContentGenerator());
    }

    public void run(String url, String basePackage) throws IOException {
        String baseTargetDir = System.getProperty("targetDir");

        //// TEMP
        baseTargetDir += System.currentTimeMillis();
        //// TEMP

        if (baseTargetDir == null || baseTargetDir.trim().isEmpty()) {
            throw new RuntimeException("Invalid target dir!");
        }

        if (new File(baseTargetDir).exists()) {
            throw new RuntimeException(
                    "Target dir already exists, please provide a non-existing target dir so it includes only the generated code: " +
                            baseTargetDir);
        }

        URLConnection urlConnection;
        InputStream is = null;
        try {
            urlConnection = new URL(url).openConnection();
            urlConnection.connect();
            is = urlConnection.getInputStream();
            ApiDetailsResponse api = new ObjectMapper().readValue(is, ApiDetailsResponse.class);

            Path targetPath = Paths.get(baseTargetDir);
            Map<Long, TypedObject> types = generateTypes(api, targetPath);
            generateMethods(api, types, targetPath, basePackage);
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private Map<Long, TypedObject> generateTypes(ApiDetailsResponse api, Path targetPath) throws IOException {
        List<TypedObject> types = api.getTypes();
        Map<Long, TypedObject> typeMap = new HashMap<>();
        for (TypedObject type : types) {
            typeMap.put(type.getId(), type);
        }

        for (TypedObject type : types) {
            type.unReferenceFields(typeMap);
        }

        File targetPathAsFile = targetPath.toFile();
        if (targetPathAsFile.exists()) {
            targetPathAsFile.delete();
        }

        Files.createDirectories(targetPath);

        for (TypedObject type : types) {
            String objectTypeClass = type.getClassName();
            if (objectTypeClass == null || objectTypeClass.indexOf('.') == -1 ||
                    objectTypeClass.indexOf('<') > -1 || objectTypeClass.indexOf('[') > -1)
            {
                System.out.println("Unhandled class: " + type);
                continue;
            }

            String targetPackage = getPackage(objectTypeClass);
            String fullClassPath = classNameToPath(objectTypeClass);
            Path tempPath = targetPath.resolve(fullClassPath);
            Path fileTargetFolder = tempPath.getParent();
            String className = tempPath.getFileName().toString();
            String contents = generateClassContents(type, targetPackage, className, typeMap);
            if (contents == null) {
                System.out.println("Invalid...");
                continue;
            }

            writeToFile(contents, fileTargetFolder, className + ".java");
        }

        return typeMap;
    }

    private void generateMethods(ApiDetailsResponse api, Map<Long, TypedObject> types, Path targetPath,
                                 String targetPackage) throws IOException
    {
        for (EndpointInfo endpointInfo : api.getEndpoints()) {
            TypedObject requestParam = endpointInfo.getRequestParam();
            if (requestParam != null) {
                endpointInfo.setRequestParam(types.get(requestParam.getId()));
            }

            TypedObject returnType = endpointInfo.getReturnType();
            if (returnType != null) {
                endpointInfo.setReturnType(types.get(returnType.getId()));
            }

            List<AnObject> queryParams = endpointInfo.getQueryParams();
            if (queryParams != null) {
                for (AnObject object : queryParams) {
                    object.setType(types.get(object.getType().getId()));
                }
            }
        }
        String fullClassName = targetPackage + '.' + "RestdlApiClient";
        String fullClassPath = classNameToPath(fullClassName);
        Path tempPath = targetPath.resolve(fullClassPath);
        Path fileTargetFolder = tempPath.getParent();
        String className = tempPath.getFileName().toString();
        generateInterface(api.getEndpoints(), targetPackage, className, fileTargetFolder, types);
    }

    private void generateInterface(List<EndpointInfo> endpoints, String packageName, String className,
                                   Path targetPath, Map<Long, TypedObject> types) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        JavaWriter.writePackage(sb, packageName);
        sb.append("public interface ").append(className).append(" {").append(ContentGenerator.EOL);
        for (EndpointInfo endpoint : endpoints) {
            sb.append(ContentGenerator.EOL);
            sb.append('\t');
            JavaWriter.writeSignatureClass(sb, endpoint.getReturnType(), types);
            sb.append(' ');
            JavaWriter.writeMethodName(sb, endpoint);
            sb.append('(');
            sb.append(')');
            sb.append(ContentGenerator.EOL_CODE);
        }
        sb.append('}').append(ContentGenerator.EOL);
        writeToFile(sb.toString(), targetPath, className + ".java");
    }

    private String getPackage(String objectTypeClass) {
        return objectTypeClass.substring(0, objectTypeClass.lastIndexOf('.'));
    }

    private String classNameToPath(String targetPackage) {
        return targetPackage.replace('.', File.separatorChar).replace('$', '_');
    }

    private String generateClassContents(TypedObject typedObject, String targetPackage, String className,
                                         Map<Long, TypedObject> typeMap)
    {
        TypedObjectType typedObjectType = TypedObjectType.fromString(typedObject.getType());
        if (contentGenerators.containsKey(typedObjectType)) {
            ContentGenerator contentGenerator = contentGenerators.get(typedObjectType);

            StringBuilder sb = new StringBuilder();
            JavaWriter.writePackage(sb, targetPackage);
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

    private void writeToFile(String contents, Path folder, String filename) throws IOException {
        Files.createDirectories(folder);
        Path file = Files.createFile(folder.resolve(filename));
        Files.write(file, contents.getBytes("UTF-8"));
    }
}
