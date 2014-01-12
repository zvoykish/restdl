package com.zvoykish.restdl.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zvoykish.restdl.java.generators.ClassContentGenerator;
import com.zvoykish.restdl.java.generators.ContentGenerator;
import com.zvoykish.restdl.java.generators.EnumObjectContentGenerator;
import com.zvoykish.restdl.java.generators.PrimitiveObjectContentGenerator;
import com.zvoykish.restdl.objects.ApiDetailsResponse;
import com.zvoykish.restdl.objects.TypedObject;
import com.zvoykish.restdl.objects.TypedObjectType;

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
            new Restdl2Java().run(args[0]);
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

    public void run(String url) throws IOException {
//        String targetPackage = "com.test";
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

        Path targetPath = Paths.get(baseTargetDir);
        URLConnection urlConnection;
        InputStream is = null;
        try {
            urlConnection = new URL(url).openConnection();
            urlConnection.connect();
            is = urlConnection.getInputStream();
            ApiDetailsResponse api = new ObjectMapper().readValue(is, ApiDetailsResponse.class);
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
                Path tempPath = Paths.get(baseTargetDir, fullClassPath);
                Path fileTargetFolder = tempPath.getParent();
                String className = tempPath.getFileName().toString();
                String contents = generateClassContents(type, targetPackage, className, typeMap);
                if (contents == null) {
                    System.out.println("Invalid...");
                    continue;
                }

                Files.createDirectories(fileTargetFolder);
                Path file = Files.createFile(fileTargetFolder.resolve(className + ".java"));
                Files.write(file, contents.getBytes("UTF-8"));
            }
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
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
            sb.append("package ").append(targetPackage).append(ContentGenerator.EOL_CODE).append(ContentGenerator.EOL);
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
