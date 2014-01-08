package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.EndpointInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 00:55
 */
public interface RestdlEngine {
    List<EndpointInfo> getEndpointsInfo();
}