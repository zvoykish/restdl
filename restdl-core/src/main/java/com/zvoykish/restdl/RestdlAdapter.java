package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

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

    List<AnObject> getMethodQueryParams(Method method);

    TypedObject getMethodRequestParam(Method method);

    Class<?> resolveTargetClass(Class<?> clazz);
}
