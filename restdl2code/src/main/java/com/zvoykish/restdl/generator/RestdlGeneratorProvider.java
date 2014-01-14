package com.zvoykish.restdl.generator;

import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/12/14
 * Time: 22:48
 */
public interface RestdlGeneratorProvider {
    boolean isIgnoredType(String objectTypeClass);

    String generateTypeContents(TypedObject type, String className, Map<Long, TypedObject> typeMap);

    String generateApiInterface(List<EndpointInfo> endpoints, String className, String packageName,
                                Map<Long, TypedObject> typeMap);

    String generateApiImplementation(List<EndpointInfo> endpoints, String className, String packageName,
                                     Map<Long, TypedObject> typeMap);

    HttpClientFactoryInterface generateHttpClientFactory(String packageName);

    String getClassFileExtension();

    String classNameToPath(String objectTypeClass);
}
