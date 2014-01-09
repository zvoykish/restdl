package com.zvoykish.restdl.adapters;

import com.zvoykish.restdl.RestdlAdapter;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 01:20
 */
public class AdapterFactoryFacade implements AdapterFactory<RestdlAdapter> {
    public static final String PROPERTY_ADAPTER_FACTORY_NAME = "restdl.adapter.factory";
    public static final String FACTORIES_PACKAGE = "com.zvoykish.restdl.adapters.factories.";

    public RestdlAdapter createAdapter(Map<String, Object> context) throws RuntimeException {
        try {
            String concreteFactoryClassName = System.getProperty(PROPERTY_ADAPTER_FACTORY_NAME);
            Class<?> concreteFactoryClass = Class.forName(FACTORIES_PACKAGE + concreteFactoryClassName);
            if (AdapterFactory.class.isAssignableFrom(concreteFactoryClass)) {
                AdapterFactory adapterFactory = (AdapterFactory) concreteFactoryClass.newInstance();
                return adapterFactory.createAdapter(context);
            }
            else {
                throw new IllegalArgumentException("Invalid RestDL adapter: " + concreteFactoryClassName);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed creating RestDL adapter", e);
        }
    }
}
