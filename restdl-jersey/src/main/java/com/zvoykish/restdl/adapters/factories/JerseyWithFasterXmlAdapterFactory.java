package com.zvoykish.restdl.adapters.factories;

import com.zvoykish.restdl.adapters.JerseyWithFasterXmlAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/17/14
 * Time: 14:12
 */
@SuppressWarnings("UnusedDeclaration")
public class JerseyWithFasterXmlAdapterFactory extends BaseJerseyWithJacksonAdapterFactory<JerseyWithFasterXmlAdapter> {
    @Override
    protected JerseyWithFasterXmlAdapter doCreate() {
        return new JerseyWithFasterXmlAdapter();
    }
}
