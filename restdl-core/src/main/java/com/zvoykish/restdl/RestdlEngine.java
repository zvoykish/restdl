package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.ApiDetailsResponse;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 00:55
 */
public interface RestdlEngine {
    ApiDetailsResponse getApiDetails(boolean inlineTypes);
}