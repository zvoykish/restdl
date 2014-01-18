package com.zvoykish.restdl.adapters.factories;

import com.zvoykish.restdl.adapters.AdapterFactory;
import com.zvoykish.restdl.adapters.BaseJerseyWithJacksonAdapter;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 02:44
 */
public abstract class BaseJerseyWithJacksonAdapterFactory<T extends BaseJerseyWithJacksonAdapter>
        implements AdapterFactory<T>
{
    @Override
    public T createAdapter(Map<String, Object> context) throws RuntimeException {
        T adapter = doCreate();
        adapter.init();
        return adapter;
    }

    protected abstract T doCreate();
}
