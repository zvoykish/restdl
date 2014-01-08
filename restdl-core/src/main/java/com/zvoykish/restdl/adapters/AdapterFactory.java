package com.zvoykish.restdl.adapters;

import com.zvoykish.restdl.RestdlAdapter;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 01:54
 */
public interface AdapterFactory<T extends RestdlAdapter> {
    T createAdapter(Map<String, Object> context) throws RuntimeException;
}
