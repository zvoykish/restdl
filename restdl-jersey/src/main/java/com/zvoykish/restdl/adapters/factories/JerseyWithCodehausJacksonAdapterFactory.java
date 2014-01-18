package com.zvoykish.restdl.adapters.factories;

import com.zvoykish.restdl.adapters.JerseyWithCodehausJacksonAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 02:43
 */
@SuppressWarnings("UnusedDeclaration")
public class JerseyWithCodehausJacksonAdapterFactory
        extends BaseJerseyWithJacksonAdapterFactory<JerseyWithCodehausJacksonAdapter>
{
    protected JerseyWithCodehausJacksonAdapter doCreate() {
        return new JerseyWithCodehausJacksonAdapter();
    }
}
