package com.zvoykish.restdl.jersey;

import com.zvoykish.restdl.*;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.types.AnObject;
import com.zvoykish.restdl.objects.types.TypedObject;
import org.reflections.Reflections;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/17/14
 * Time: 14:12
 */
public class RestdlJerseyAdapter extends BaseRestdlAdapter implements RestdlAdapter {
    private final ParameterNameDiscoverer parameterNameDiscoverer;
    private final JacksonTargetClassResolver effectiveClassResolver;
    private TypeHelper typeHelper;

    public RestdlJerseyAdapter() {
        parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        effectiveClassResolver = new JacksonTargetClassResolver();
    }

    public void init() {
        typeHelper = new TypeHelperImpl(this);
    }

    @Override
    public Collection<Class> getWebControllers() {
        List<Class> classes = new ArrayList<>();
        Reflections reflections = new Reflections(getBasePackage());
        classes.addAll(reflections.getTypesAnnotatedWith(Path.class));
        Collections.sort(classes, new Comparator<Class>() {
            @Override
            public int compare(Class o1, Class o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return classes;
    }

    @Override
    public String getControllerBaseUrl(Class<?> controllerClass) {
        String baseUrl = "";
        Path controllerPath = controllerClass.getAnnotation(Path.class);
        if (controllerPath != null) {
            String value = controllerPath.value();
            if (value != null && !value.trim().isEmpty()) {
                baseUrl = value;
            }
        }
        return baseUrl;
    }

    @Override
    public String getMethodUrl(Method method) {
        String methodUrl = "";
        Path controllerPath = method.getAnnotation(Path.class);
        if (controllerPath != null) {
            String value = controllerPath.value();
            if (value != null && !value.trim().isEmpty()) {
                methodUrl = value;
            }
        }
        return methodUrl;
    }

    @Override
    public EndpointInfo.HttpMethod getMethodHttpMethod(Method method) {
        if (method.isAnnotationPresent(POST.class)) {
            return EndpointInfo.HttpMethod.POST;
        }
        else if (method.isAnnotationPresent(PUT.class)) {
            return EndpointInfo.HttpMethod.PUT;
        }
        else if (method.isAnnotationPresent(DELETE.class)) {
            return EndpointInfo.HttpMethod.DELETE;
        }
        else {
            return EndpointInfo.HttpMethod.GET;
        }
    }

    @Override
    public List<AnObject> getMethodPathParams(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        List<AnObject> pathParams = new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Type[] parameterTypes = method.getGenericParameterTypes();
        String[] names = null;
        if (parameterAnnotations != null && parameterAnnotations.length > 0) {
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] currAnns = parameterAnnotations[i];
                if (currAnns != null && currAnns.length > 0) {
                    for (Annotation annotation : currAnns) {
                        Type paramType = parameterTypes[i];
                        if (annotation instanceof PathParam) {
                            String paramName = ((PathParam) annotation).value();
                            if (paramName != null && !paramName.trim().isEmpty()) {
                                if (names == null) {
                                    names = parameterNameDiscoverer.getParameterNames(method);
                                }
                                paramName = names[i];
                            }

                            TypedObject type = typeHelper.typeToAType(paramType, objects);
                            pathParams.add(new AnObject(paramName, type));
                        }
                    }
                }
            }
        }

        Collections.sort(pathParams, new Comparator<AnObject>() {
            @Override
            public int compare(AnObject o1, AnObject o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return pathParams;
    }

    @Override
    public List<AnObject> getMethodQueryParams(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        List<AnObject> queryParams = new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Type[] parameterTypes = method.getGenericParameterTypes();
        String[] names = null;
        if (parameterAnnotations != null && parameterAnnotations.length > 0) {
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] currAnns = parameterAnnotations[i];
                if (currAnns != null && currAnns.length > 0) {
                    Type paramType = parameterTypes[i];
                    String paramName = null;
                    String defaultValue = null;
                    for (Annotation annotation : currAnns) {
                        if (annotation instanceof QueryParam) {
                            QueryParam param = (QueryParam) annotation;
                            paramName = param.value();
                            if (paramName == null || paramName.trim().isEmpty()) {
                                if (names == null) {
                                    names = parameterNameDiscoverer.getParameterNames(method);
                                }
                                paramName = names[i];
                            }
                        }
                        else if (annotation instanceof DefaultValue) {
                            defaultValue = ((DefaultValue) annotation).value();
                        }
                    }

                    if (paramName != null && !paramName.trim().isEmpty()) {
                        TypedObject type = typeHelper.typeToAType(paramType, objects);
                        queryParams.add(new AnObject(paramName, type, defaultValue));
                    }
                }
            }
        }

        Collections.sort(queryParams, new Comparator<AnObject>() {
            @Override
            public int compare(AnObject o1, AnObject o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return queryParams;
    }

    @Override
    public TypedObject getMethodRequestParam(Method method, Map<String, AtomicReference<TypedObject>> objects) {
        TypedObject requestParam = null;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Type[] parameterTypes = method.getGenericParameterTypes();
        if (parameterAnnotations != null && parameterAnnotations.length > 0) {
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] currAnns = parameterAnnotations[i];
                if (currAnns == null || currAnns.length == 0) {
                    Type paramType = parameterTypes[i];
                    requestParam = typeHelper.typeToAType(paramType, objects);
                }
            }
        }

        return requestParam;
    }

    @Override
    public Class<?> resolveTargetClass(Class<?> clazz) {
        return effectiveClassResolver.resolveTargetClass(clazz);
    }
}
