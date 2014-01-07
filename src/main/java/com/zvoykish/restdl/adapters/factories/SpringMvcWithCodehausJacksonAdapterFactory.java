package com.zvoykish.restdl.adapters.factories;

import com.zvoykish.restdl.adapters.SpringMvcWithCodehausJacksonAdapter;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 01:54
 */
@SuppressWarnings("UnusedDeclaration")
public class SpringMvcWithCodehausJacksonAdapterFactory
        extends BaseSpringWithJacksonAdapterFactory<SpringMvcWithCodehausJacksonAdapter>
{
    protected SpringMvcWithCodehausJacksonAdapter doCreate(ApplicationContext applicationContext) {
        return new SpringMvcWithCodehausJacksonAdapter(applicationContext);
    }
}
