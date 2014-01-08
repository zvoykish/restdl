package com.zvoykish.restdl.adapters;

import com.zvoykish.restdl.adapters.partials.PartialCodehausJacksonAdapter;
import com.zvoykish.restdl.adapters.partials.PartialSpringMvcAdapter;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 00:59
 */
public class SpringMvcWithCodehausJacksonAdapter extends BaseSpringWithJacksonAdapter {
    public SpringMvcWithCodehausJacksonAdapter(ApplicationContext applicationContext) {
        super(applicationContext.getBean(PartialSpringMvcAdapter.class), new PartialCodehausJacksonAdapter());
    }
}
