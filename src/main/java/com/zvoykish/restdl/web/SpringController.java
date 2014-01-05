package com.zvoykish.restdl.web;

import com.zvoykish.restdl.annotations.Description;
import com.zvoykish.restdl.annotations.UsedBy;
import com.zvoykish.restdl.objects.*;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/5/14
 * Time: 20:55
 */
@Controller
@RequestMapping(value = "/api/restdl")
public class SpringController {
    @Autowired
    private ApplicationContext context;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Description("Retrieves information about all endpoints defined by Spring MVC controllers")
    @UsedBy("testing")
    public List<EndpointInfo> getSystemInfo() {
        List<EndpointInfo> methods = new ArrayList<EndpointInfo>();
        Map<String, Object> controllerMap = context.getBeansWithAnnotation(Controller.class);
        for (Object controller : controllerMap.values()) {
            Class<?> controllerClass = controller.getClass();
            RequestMapping controllerMapping = controllerClass.getAnnotation(RequestMapping.class);
            if (controllerMapping != null) {
                String baseUrl = "" + controllerMapping.value()[0];
                for (Method method : controllerClass.getDeclaredMethods()) {
                    RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                    if (methodMapping != null) {
                        EndpointInfo endpointInfo = new EndpointInfo();

                        Description description = method.getAnnotation(Description.class);
                        if (description != null && !StringUtils.isEmpty(description.value())) {
                            endpointInfo.setDescription(description.value());
                        }

                        RequestMethod[] requestMethods = methodMapping.method();
                        if (requestMethods != null && requestMethods.length > 0) {
                            endpointInfo.setMethod(requestMethods[0].name());
                        }
                        String[] values = methodMapping.value();
                        if (values != null && values.length > 0) {
                            endpointInfo.setUrl(baseUrl + values[0]);
                        }
                        else {
                            endpointInfo.setUrl(baseUrl);
                        }

                        List<AnObject> queryParams = new ArrayList<AnObject>();
                        TypedObject requestParam = null;

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
                                            queryParams.add(new AnObject(param.value(), typeToAType(paramType),
                                                    param.defaultValue()));
                                        }
                                        else if (annotation instanceof RequestBody) {
                                            requestParam = typeToAType(paramType);
                                        }
                                    }
                                }
                            }
                        }

                        if (!queryParams.isEmpty()) {
                            endpointInfo.setQueryParams(queryParams);
                        }

                        if (requestParam != null) {
                            endpointInfo.setRequestParam(requestParam);
                        }

                        Type returnType = method.getGenericReturnType();
                        TypedObject aReturnType = typeToAType(returnType);
                        endpointInfo.setReturnType(aReturnType);

                        UsedBy usedBy = method.getAnnotation(UsedBy.class);
                        if (usedBy != null && !StringUtils.isEmpty(usedBy.value())) {
                            endpointInfo.setUsedBy(usedBy.value());
                        }

                        methods.add(endpointInfo);
                    }
                }
            }
        }

        Collections.sort(methods);
        return methods;
    }

    private TypedObject typeToAType(Type paramType) {
        if (paramType instanceof Class) {
            Class clazz = (Class) paramType;
            if (clazz.isInterface() && clazz.isAnnotationPresent(JsonDeserialize.class)) {
                JsonDeserialize annotation = (JsonDeserialize) clazz.getAnnotation(JsonDeserialize.class);
                Class<?> targetClass = annotation.as();
                if (!clazz.equals(targetClass)) {
                    return typeToAType(targetClass);
                }
            }

            Package pkg = clazz.getPackage();
            if (!clazz.isEnum() && !clazz.isPrimitive() && pkg != null && pkg.getName().startsWith("com.nextinline")) {
                Field[] allFields = clazz.getDeclaredFields();
                return new ComplexObject(clazz.getName(), fieldsToAnObjects(allFields));
            }
            else {
                return new PrimitiveObject(clazz.getName());
            }
        }
        else if (paramType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) paramType;
            Type rawType = parameterizedType.getRawType();
            Type[] types = parameterizedType.getActualTypeArguments();
            if (Collection.class.isAssignableFrom((Class) rawType)) {
                return new CollectionObject(typeToAType(types[0]));
            }
            else if (Map.class.isAssignableFrom((Class) rawType)) {
                return new MapObject(typeToAType(types[0]), typeToAType(types[1]));
            }
            else {
                return typeToAType(types[0]);
            }
        }

        Field[] allFields = paramType.getClass().getDeclaredFields();
        return new ComplexObject(paramType.getClass().getName(), fieldsToAnObjects(allFields));
    }

    private List<AnObject> fieldsToAnObjects(Field[] allFields) {
        List<AnObject> result = new ArrayList<AnObject>();
        for (Field field : allFields) {
            result.add(new AnObject(field.getName(), typeToAType(field.getGenericType())));
        }
        return result;
    }
}
