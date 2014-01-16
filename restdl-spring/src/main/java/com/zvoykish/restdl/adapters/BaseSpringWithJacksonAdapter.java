package com.zvoykish.restdl.adapters;

import com.zvoykish.restdl.RestdlAdapter;
import com.zvoykish.restdl.TypeHelperImpl;
import com.zvoykish.restdl.adapters.partials.PartialSpringMvcAdapter;
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
public abstract class BaseSpringWithJacksonAdapter implements RestdlAdapter {
    private PartialSpringMvcAdapter springMvcAdapter;
    private RestdlAdapter jacksonAdapter;

    protected BaseSpringWithJacksonAdapter(PartialSpringMvcAdapter springMvcAdapter, RestdlAdapter jacksonAdapter) {
        this.springMvcAdapter = springMvcAdapter;
        this.jacksonAdapter = jacksonAdapter;
    }

    public void init() {
        springMvcAdapter.setTypeHelper(new TypeHelperImpl(this));
    }

    @Override
    public String getBasePackage() {
        return springMvcAdapter.getBasePackage();
    }

    @Override
    public Collection<Class> getWebControllers() {
        return springMvcAdapter.getWebControllers();
    }

    @Override
    public String getControllerBaseUrl(Class<?> controllerClass) {
        return springMvcAdapter.getControllerBaseUrl(controllerClass);
    }

    @Override
    public String getMethodUrl(Method method) {
        return springMvcAdapter.getMethodUrl(method);
    }

    @Override
    public EndpointInfo.HttpMethod getMethodHttpMethod(Method method) {
        return springMvcAdapter.getMethodHttpMethod(method);
    }

    @Override
    public List<AnObject> getMethodPathParams(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        return springMvcAdapter.getMethodPathParams(method, objects);
    }

    @Override
    public List<AnObject> getMethodQueryParams(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        return springMvcAdapter.getMethodQueryParams(method, objects);
    }

    @Override
    public TypedObject getMethodRequestParam(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        return springMvcAdapter.getMethodRequestParam(method, objects);
    }

    @Override
    public Class<?> resolveTargetClass(Class<?> clazz) {
        return jacksonAdapter.resolveTargetClass(clazz);
    }
}
