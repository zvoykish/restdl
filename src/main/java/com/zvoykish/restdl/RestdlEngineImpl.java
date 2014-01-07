package com.zvoykish.restdl;

import com.zvoykish.restdl.annotations.Description;
import com.zvoykish.restdl.annotations.UsedBy;
import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public List<EndpointInfo> getEndpointsInfo() {
        List<EndpointInfo> methods = new ArrayList<>();
        Collection<Class> webControllers = adapter.getWebControllers();
        for (Class controllerClass : webControllers) {
            if (controllerClass.getPackage().getName().startsWith("com.zvoykish.restdl.controllers")) {
                continue;
            }

            String baseUrl = adapter.getControllerBaseUrl(controllerClass);
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (!Modifier.isPublic(method.getModifiers())) {
                    continue;
                }

                EndpointInfo endpointInfo = new EndpointInfo();

                Description description = method.getAnnotation(Description.class);
                if (description != null && description.value() != null && description.value().length() > 0) {
                    endpointInfo.setDescription(description.value());
                }

                EndpointInfo.HttpMethod httpMethod = adapter.getMethodHttpMethod(method);
                if (httpMethod != null) {
                    endpointInfo.setHttpMethod(httpMethod);
                }

                String url = adapter.getMethodUrl(method);
                if (url != null && url.length() > 0) {
                    endpointInfo.setUrl(baseUrl + url);
                }
                else {
                    endpointInfo.setUrl(baseUrl);
                }

                List<AnObject> queryParams = adapter.getMethodQueryParams(method);
                if (!queryParams.isEmpty()) {
                    endpointInfo.setQueryParams(queryParams);
                }

                TypedObject requestParam = adapter.getMethodRequestParam(method);
                if (requestParam != null) {
                    endpointInfo.setRequestParam(requestParam);
                }

                Type returnType = method.getGenericReturnType();
                TypedObject aReturnType = typeHelper.typeToAType(returnType);
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
}
