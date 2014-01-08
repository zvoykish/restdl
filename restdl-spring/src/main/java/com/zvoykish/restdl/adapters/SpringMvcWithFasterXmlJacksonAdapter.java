package com.zvoykish.restdl.adapters;

import com.zvoykish.restdl.adapters.partials.PartialFasterXmlJacksonAdapter;
import com.zvoykish.restdl.adapters.partials.PartialSpringMvcAdapter;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 02:38
 */
public class SpringMvcWithFasterXmlJacksonAdapter extends BaseSpringWithJacksonAdapter {
    public SpringMvcWithFasterXmlJacksonAdapter(ApplicationContext applicationContext) {
        super(applicationContext.getBean(PartialSpringMvcAdapter.class), new PartialFasterXmlJacksonAdapter());
    }
}
