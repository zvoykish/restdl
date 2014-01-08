package com.zvoykish.restdl.adapters.factories;

import com.zvoykish.restdl.adapters.AdapterFactory;
import com.zvoykish.restdl.adapters.BaseSpringWithJacksonAdapter;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 02:44
 */
public abstract class BaseSpringWithJacksonAdapterFactory<T extends BaseSpringWithJacksonAdapter>
        implements AdapterFactory<T>
{
    public static final String APP_CTX = "applicationContext";

    @Override
    public T createAdapter(Map<String, Object> context) throws RuntimeException {
        Object obj = context.get(APP_CTX);
        if (obj instanceof ApplicationContext) {
            ApplicationContext applicationContext = (ApplicationContext) obj;
            T adapter = doCreate(applicationContext);
            adapter.init();
            return adapter;
        }

        String factoryName = getClass().getSimpleName();
        throw new IllegalArgumentException(factoryName + " requires a valid Spring " + APP_CTX + " parameter");
    }

    protected abstract T doCreate(ApplicationContext applicationContext);
}
