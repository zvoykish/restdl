package com.zvoykish.restdl;

import com.zvoykish.restdl.annotations.Description;
import com.zvoykish.restdl.annotations.UsedBy;
import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.ApiDetailsResponse;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 00:24
 */
public class RestdlEngineImpl implements RestdlEngine {
    private RestdlAdapter adapter;
    private TypeHelper typeHelper;

    public RestdlEngineImpl(RestdlAdapter adapter) {
        this.adapter = adapter;
        this.typeHelper = new TypeHelperImpl(adapter);
    }

    @Override
    public ApiDetailsResponse getApiDetails(boolean inlineTypes) {
        TypeHelper.INLINE_TYPES.set(inlineTypes);
        try {
            Map<String, AtomicReference<TypedObject>> objects = new HashMap<>();
            List<EndpointInfo> methods = generateEndpointInfo(objects);
            List<TypedObject> types = generateObjectInfo(objects);
            updateReferences(methods, types);
            ApiDetailsResponse response = new ApiDetailsResponse(methods, types);
            response.calculateMd5();
            return response;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            TypeHelper.INLINE_TYPES.remove();
        }
    }

    private void updateReferences(List<EndpointInfo> methods, List<TypedObject> types) {
        Map<Long, TypedObject> typesById = new HashMap<>();
        for (TypedObject type : types) {
            typesById.put(type.getId(), type);
        }

        for (TypedObject type : types) {
            type.referenceFields(typesById);
        }

        for (EndpointInfo method : methods) {
            method.updateReferences(typesById);
        }
    }

    private List<EndpointInfo> generateEndpointInfo(Map<String, AtomicReference<TypedObject>> objects) {
        List<EndpointInfo> methods = new ArrayList<>();
        Collection<Class> webControllers = adapter.getWebControllers();
        for (Class controllerClass : webControllers) {
            if (controllerClass.getPackage().getName().startsWith(getClass().getPackage().getName())) {
                continue;
            }

            String baseUrl = adapter.getControllerBaseUrl(controllerClass);
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (!Modifier.isPublic(method.getModifiers())) {
                    continue;
                }

                EndpointInfo endpointInfo = new EndpointInfo();

                endpointInfo.setControllerName(controllerClass.getSimpleName());
                endpointInfo.setMethodName(method.getName());

                Description description = method.getAnnotation(Description.class);
                if (description != null && description.value() != null && description.value().length() > 0) {
                    endpointInfo.setDescription(description.value());
                }

                EndpointInfo.HttpMethod httpMethod = adapter.getMethodHttpMethod(method);
                if (httpMethod != null) {
                    endpointInfo.setHttpMethod(httpMethod);
                }

                String methodUrl = adapter.getMethodUrl(method);
                if (methodUrl != null && !methodUrl.trim().isEmpty()) {
                    endpointInfo.setUrl(normalizeUrl(baseUrl, methodUrl));
                }
                else if (baseUrl != null && !baseUrl.trim().isEmpty()) {
                    endpointInfo.setUrl(baseUrl);
                }
                else {
                    endpointInfo.setUrl("/");
                }

                List<AnObject> pathParams = adapter.getMethodPathParams(method, objects);
                if (!pathParams.isEmpty()) {
                    endpointInfo.setPathParams(pathParams);
                }

                List<AnObject> queryParams = adapter.getMethodQueryParams(method, objects);
                if (!queryParams.isEmpty()) {
                    endpointInfo.setQueryParams(queryParams);
                }

                TypedObject requestParam = adapter.getMethodRequestParam(method, objects);
                if (requestParam != null) {
                    endpointInfo.setRequestParam(requestParam);
                }

                Type returnType = method.getGenericReturnType();
                TypedObject aReturnType = typeHelper.typeToAType(returnType, objects);
                endpointInfo.setReturnType(aReturnType);

                UsedBy usedBy = method.getAnnotation(UsedBy.class);
                if (usedBy != null && usedBy.value() != null && usedBy.value().length() > 0) {
                    endpointInfo.setUsedBy(usedBy.value());
                }

                methods.add(endpointInfo);
            }
        }

        Collections.sort(methods);
        return methods;
    }

    private String normalizeUrl(String s1, String s2) {
        if (s1.endsWith("/") && s2.startsWith("/")) {
            return s1 + s2.substring(1);
        }
        else {
            return s1 + s2;
        }
    }

    private List<TypedObject> generateObjectInfo(Map<String, AtomicReference<TypedObject>> objects) {
        List<TypedObject> types = new ArrayList<>();
        for (AtomicReference<TypedObject> reference : objects.values()) {
            TypedObject object = reference.get();
            if (object != null) {
                types.add(object);
            }
        }

        Collections.sort(types, new Comparator<TypedObject>() {
            @Override
            public int compare(TypedObject o1, TypedObject o2) {
                return Long.compare(o1.getId(), o2.getId());
            }
        });

        return types;
    }
}
