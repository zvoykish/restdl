package com.zvoykish.restdl.jersey;

import com.zvoykish.restdl.RestdlAdapter;
import com.zvoykish.restdl.RestdlEngine;
import com.zvoykish.restdl.RestdlEngineImpl;
import com.zvoykish.restdl.factories.AdapterFactoryFacade;
import com.zvoykish.restdl.annotations.Description;
import com.zvoykish.restdl.annotations.UsedBy;
import com.zvoykish.restdl.objects.ApiDetailsResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/5/14
 * Time: 20:55
 */
@SuppressWarnings("UnusedDeclaration")
@Path("/api/restdl")
@Produces(MediaType.APPLICATION_JSON)
public class RestdlJerseyController {
    private RestdlEngine engine;

    public RestdlJerseyController() {
        RestdlAdapter adapter = new AdapterFactoryFacade().createAdapter(new HashMap<String, Object>());
        engine = new RestdlEngineImpl(adapter);
    }

    @GET
    @Description("Retrieves information about all endpoints defined by Spring MVC controllers")
    @UsedBy("testing")
    public ApiDetailsResponse getSystemInfo(@QueryParam("inlineTypes") @DefaultValue("false") boolean inlineTypes) {
        return engine.getApiDetails(inlineTypes);
    }
}
