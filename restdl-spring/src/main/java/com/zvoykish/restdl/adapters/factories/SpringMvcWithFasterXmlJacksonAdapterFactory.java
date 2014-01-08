package com.zvoykish.restdl.adapters.factories;

import com.zvoykish.restdl.adapters.SpringMvcWithFasterXmlJacksonAdapter;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 02:43
 */
@SuppressWarnings("UnusedDeclaration")
public class SpringMvcWithFasterXmlJacksonAdapterFactory
        extends BaseSpringWithJacksonAdapterFactory<SpringMvcWithFasterXmlJacksonAdapter>
{
    protected SpringMvcWithFasterXmlJacksonAdapter doCreate(ApplicationContext applicationContext) {
        return new SpringMvcWithFasterXmlJacksonAdapter(applicationContext);
    }
}
