package com.zvoykish.restdl.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zvoykish.restdl.generator.java.JavaRestdlGeneratorProvider;
import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.ApiDetailsResponse;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;

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
public class Restdl2Code {
    private final RestdlGeneratorProvider provider;

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.out.println(
                    "Invalid arguments. Please use the following syntax: Restdl2Code [Restdl URL] [Target package for client]");
            System.exit(1);
        }

        try {
            new Restdl2Code().run(args[0], args[1]);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(99);
        }
    }

    public Restdl2Code() {
        provider = new JavaRestdlGeneratorProvider();
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
            if (provider.isIgnoredType(objectTypeClass)) {
                System.out.println("Unhandled class: " + type);
                continue;
            }

            String fullClassPath = provider.classNameToPath(objectTypeClass);
            Path tempPath = targetPath.resolve(fullClassPath);
            Path fileTargetFolder = tempPath.getParent();
            String className = tempPath.getFileName().toString();
            String contents = provider.generateTypeContents(type, className, typeMap);
            if (contents == null) {
                System.out.println("Invalid...");
                continue;
            }

            writeToFile(contents, fileTargetFolder, className + '.' + provider.getClassFileExtension());
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
        String fullClassPath = provider.classNameToPath(fullClassName);
        Path tempPath = targetPath.resolve(fullClassPath);
        Path fileTargetFolder = tempPath.getParent();
        String className = tempPath.getFileName().toString();
        generateApiInterface(api.getEndpoints(), targetPackage, className, fileTargetFolder, types);
        generateApiImplementation(api.getEndpoints(), targetPackage, className + "Impl", fileTargetFolder, types);
    }

    private void generateApiImplementation(List<EndpointInfo> endpoints, String packageName, String className,
                                           Path targetPath, Map<Long, TypedObject> types) throws IOException
    {
        String content = provider.generateApiImplementation(endpoints, className, packageName, types);
        writeToFile(content, targetPath, className + '.' + provider.getClassFileExtension());
    }

    private void generateApiInterface(List<EndpointInfo> endpoints, String packageName, String className,
                                      Path targetPath, Map<Long, TypedObject> types) throws IOException
    {
        String content = provider.generateApiInterface(endpoints, className, packageName, types);
        writeToFile(content, targetPath, className + '.' + provider.getClassFileExtension());
    }

    private void writeToFile(String contents, Path folder, String filename) throws IOException {
        Files.createDirectories(folder);
        Path file = Files.createFile(folder.resolve(filename));
        Files.write(file, contents.getBytes("UTF-8"));
    }
}
