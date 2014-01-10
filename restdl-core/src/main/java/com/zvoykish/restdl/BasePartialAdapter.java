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
 * Time: 01:07
 */
public abstract class BasePartialAdapter implements RestdlAdapter {
    public String getBasePackage() {
        throw new UnsupportedOperationException("Unsupported by adapter");
    }

    public Collection<Class> getWebControllers() {
        throw new UnsupportedOperationException("Unsupported by adapter");
    }

    public String getControllerBaseUrl(Class<?> controllerClass) {
        throw new UnsupportedOperationException("Unsupported by adapter");
    }

    public String getMethodUrl(Method method) {
        throw new UnsupportedOperationException("Unsupported by adapter");
    }

    public EndpointInfo.HttpMethod getMethodHttpMethod(Method method) {
        throw new UnsupportedOperationException("Unsupported by adapter");
    }

    public List<AnObject> getMethodQueryParams(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        throw new UnsupportedOperationException("Unsupported by adapter");
    }

    public TypedObject getMethodRequestParam(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        throw new UnsupportedOperationException("Unsupported by adapter");
    }

    public Class<?> resolveTargetClass(Class<?> clazz) {
        throw new UnsupportedOperationException("Unsupported by adapter");
    }
}
