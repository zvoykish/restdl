package com.zvoykish.restdl.adapters;

import com.zvoykish.restdl.RestdlAdapter;
import com.zvoykish.restdl.TypeHelperImpl;
import com.zvoykish.restdl.adapters.partials.PartialJerseyAdapter;
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
 * Time: 02:38
 */
public abstract class BaseJerseyWithJacksonAdapter implements RestdlAdapter {
    private PartialJerseyAdapter jerseyAdapter;
    private RestdlAdapter jacksonAdapter;

    protected BaseJerseyWithJacksonAdapter(PartialJerseyAdapter jerseyAdapter, RestdlAdapter jacksonAdapter) {
        this.jerseyAdapter = jerseyAdapter;
        this.jacksonAdapter = jacksonAdapter;
    }

    public void init() {
        jerseyAdapter.setTypeHelper(new TypeHelperImpl(this));
    }

    @Override
    public String getBasePackage() {
        return jerseyAdapter.getBasePackage();
    }

    @Override
    public Collection<Class> getWebControllers() {
        return jerseyAdapter.getWebControllers();
    }

    @Override
    public String getControllerBaseUrl(Class<?> controllerClass) {
        return jerseyAdapter.getControllerBaseUrl(controllerClass);
    }

    @Override
    public String getMethodUrl(Method method) {
        return jerseyAdapter.getMethodUrl(method);
    }

    @Override
    public EndpointInfo.HttpMethod getMethodHttpMethod(Method method) {
        return jerseyAdapter.getMethodHttpMethod(method);
    }

    @Override
    public List<AnObject> getMethodPathParams(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        return jerseyAdapter.getMethodPathParams(method, objects);
    }

    @Override
    public List<AnObject> getMethodQueryParams(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        return jerseyAdapter.getMethodQueryParams(method, objects);
    }

    @Override
    public TypedObject getMethodRequestParam(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        return jerseyAdapter.getMethodRequestParam(method, objects);
    }

    @Override
    public Class<?> resolveTargetClass(Class<?> clazz) {
        return jacksonAdapter.resolveTargetClass(clazz);
    }
}
