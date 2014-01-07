package com.zvoykish.restdl.controllers;

import com.zvoykish.restdl.RestdlAdapter;
import com.zvoykish.restdl.RestdlEngine;
import com.zvoykish.restdl.RestdlEngineImpl;
import com.zvoykish.restdl.adapters.AdapterFactoryFacade;
import com.zvoykish.restdl.annotations.Description;
import com.zvoykish.restdl.annotations.UsedBy;
import com.zvoykish.restdl.objects.EndpointInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/5/14
 * Time: 20:55
 */
@Controller
@RequestMapping(value = "/api/restdl")
public class SpringMvcController implements InitializingBean {
    private RestdlEngine engine;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Description("Retrieves information about all endpoints defined by Spring MVC controllers")
    @UsedBy("testing")
    public List<EndpointInfo> getSystemInfo() {
        return engine.getEndpointsInfo();
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        if (engine == null) {
            Map<String, Object> context = new HashMap<>();
            context.put("applicationContext", applicationContext);
            RestdlAdapter adapter = new AdapterFactoryFacade().createAdapter(context);
            engine = new RestdlEngineImpl(adapter);
        }
    }
}
