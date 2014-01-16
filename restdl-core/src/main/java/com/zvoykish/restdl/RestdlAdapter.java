package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 00:22
 */
public interface RestdlAdapter {
    String getBasePackage();

    Collection<Class> getWebControllers();

    String getControllerBaseUrl(Class<?> controllerClass);

    String getMethodUrl(Method method);

    EndpointInfo.HttpMethod getMethodHttpMethod(Method method);

    List<AnObject> getMethodPathParams(Method method, Map<String, AtomicReference<TypedObject>> objects);

    List<AnObject> getMethodQueryParams(Method method, Map<String, AtomicReference<TypedObject>> objects);

    TypedObject getMethodRequestParam(Method method, Map<String, AtomicReference<TypedObject>> objects);

    Class<?> resolveTargetClass(Class<?> clazz);
}
