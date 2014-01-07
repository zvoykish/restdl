package com.zvoykish.restdl.adapters.partials;

import com.zvoykish.restdl.BasePartialAdapter;
import com.zvoykish.restdl.TypeHelper;
import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.EndpointInfo;
import com.zvoykish.restdl.objects.TypedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 00:41
 */
@Component
public class PartialSpringMvcAdapter extends BasePartialAdapter {
    @Value("${com.zvoykish.restdl.base.package}")
    private String basePackage;

    @Autowired
    private ApplicationContext context;

    private TypeHelper typeHelper;

    public String getBasePackage() {
        return basePackage;
    }

    public Collection<Class> getWebControllers() {
        Map<String, Object> controllerMap = context.getBeansWithAnnotation(Controller.class);
        Set<Class> classes = new HashSet<>();
        for (Object controller : controllerMap.values()) {
            classes.add(controller.getClass());
        }
        return classes;
    }

    public String getControllerBaseUrl(Class<?> controllerClass) {
        String baseUrl = null;
        RequestMapping controllerMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (controllerMapping != null) {
            String[] values = controllerMapping.value();
            if (values != null && values.length > 0) {
                baseUrl = values[0];
            }
        }
        return baseUrl;
    }

    public String getMethodUrl(Method method) {
        String methodUrl = null;
        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
        if (methodMapping != null) {
            String[] values = methodMapping.value();
            if (values != null && values.length > 0) {
                methodUrl = values[0];
            }
        }
        return methodUrl;
    }

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

    public List<AnObject> getMethodQueryParams(Method method) {
        List<AnObject> queryParams = new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Type[] parameterTypes = method.getGenericParameterTypes();
        if (parameterAnnotations != null && parameterAnnotations.length > 0) {
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] currAnns = parameterAnnotations[i];
                if (currAnns != null && currAnns.length > 0) {
                    for (Annotation annotation : currAnns) {
                        Type paramType = parameterTypes[i];
                        if (annotation instanceof RequestParam) {
                            RequestParam param = (RequestParam) annotation;
                            TypedObject type = typeHelper.typeToAType(paramType);
                            queryParams.add(new AnObject(param.value(), type, param.defaultValue()));
                        }
                    }
                }
            }
        }

        return queryParams;
    }

    public TypedObject getMethodRequestParam(Method method) {
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
                            requestParam = typeHelper.typeToAType(paramType);
                            break;
                        }
                    }
                }
            }
        }

        return requestParam;
    }

    public Class<?> resolveTargetClass(Class<?> clazz) {
        throw new UnsupportedOperationException("Class should not be used as a whole, please use as composite");
    }

    public void setTypeHelper(TypeHelper typeHelper) {
        this.typeHelper = typeHelper;
    }
}
