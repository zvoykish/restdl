package com.zvoykish.restdl.factories;

import com.zvoykish.restdl.jersey.RestdlJerseyAdapter;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/17/14
 * Time: 14:12
 */
@SuppressWarnings("UnusedDeclaration")
public class JerseyAdapterFactory implements AdapterFactory<RestdlJerseyAdapter> {
    @Override
    public RestdlJerseyAdapter createAdapter(Map<String, Object> context) throws RuntimeException {
        RestdlJerseyAdapter adapter = new RestdlJerseyAdapter();
        adapter.init();
        return adapter;
    }
}
