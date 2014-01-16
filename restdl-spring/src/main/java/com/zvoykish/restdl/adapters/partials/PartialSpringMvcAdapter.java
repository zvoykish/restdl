package com.zvoykish.restdl.adapters.partials;

import com.zvoykish.restdl.BasePartialAdapter;
import com.zvoykish.restdl.TypeHelper;
import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 00:41
 */
@Component
public class PartialSpringMvcAdapter extends BasePartialAdapter {
    @Autowired
    private ApplicationContext context;

    private TypeHelper typeHelper;

    private String basePackage = System.getProperty("restdl.base.package");
    private ParameterNameDiscoverer parameterNameDiscoverer;

    public PartialSpringMvcAdapter() {
        parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    @Override
    public String getBasePackage() {
        return basePackage;
    }

    @Override
    public Collection<Class> getWebControllers() {
        Collection<Class> classes = new HashSet<>();
        Map<String, Object> controllerMap = context.getBeansWithAnnotation(Controller.class);
        for (Object controller : controllerMap.values()) {
            classes.add(controller.getClass());
        }
        controllerMap = context.getBeansWithAnnotation(RequestMapping.class);
        for (Object controller : controllerMap.values()) {
            classes.add(controller.getClass());
        }

        classes = new ArrayList<>(classes);
        Collections.sort((List<Class>) classes, new Comparator<Class>() {
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
        RequestMapping controllerMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (controllerMapping != null) {
            String[] values = controllerMapping.value();
            if (values != null && values.length > 0) {
                baseUrl = values[0];
            }
        }
        return baseUrl;
    }

    @Override
    public String getMethodUrl(Method method) {
        String methodUrl = "";
        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
        if (methodMapping != null) {
            String[] values = methodMapping.value();
            if (values != null && values.length > 0) {
                methodUrl = values[0];
            }
        }
        return methodUrl;
    }

    @Override
    public EndpointInfo.HttpMethod getMethodHttpMethod(Method method) {
        EndpointInfo.HttpMethod httpMethod = null;
        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
        if (methodMapping != null) {
            RequestMethod[] requestMethods = methodMapping.method();
            if (requestMethods != null && requestMethods.length > 0) {
                httpMethod = EndpointInfo.HttpMethod.valueOf(requestMethods[0].name());
            }
        }
        return httpMethod;
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
                        if (annotation instanceof PathVariable) {
                            String paramName = ((PathVariable) annotation).value();
                            if (StringUtils.isEmpty(paramName)) {
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
                    for (Annotation annotation : currAnns) {
                        Type paramType = parameterTypes[i];
                        if (annotation instanceof RequestParam) {
                            RequestParam param = (RequestParam) annotation;
                            String paramName = param.value();
                            if (StringUtils.isEmpty(paramName)) {
                                if (names == null) {
                                    names = parameterNameDiscoverer.getParameterNames(method);
                                }
                                paramName = names[i];
                            }

                            TypedObject type = typeHelper.typeToAType(paramType, objects);
                            queryParams.add(new AnObject(paramName, type, param.defaultValue()));
                        }
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
                if (currAnns != null && currAnns.length > 0) {
                    for (Annotation annotation : currAnns) {
                        Type paramType = parameterTypes[i];
                        if (annotation instanceof RequestBody) {
                            requestParam = typeHelper.typeToAType(paramType, objects);
                            break;
                        }
                    }
                }
            }
        }

        return requestParam;
    }

    @Override
    public Class<?> resolveTargetClass(Class<?> clazz) {
        throw new UnsupportedOperationException("Class should not be used as a whole, please use as composite");
    }

    public void setTypeHelper(TypeHelper typeHelper) {
        this.typeHelper = typeHelper;
    }
}
