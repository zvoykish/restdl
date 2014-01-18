package com.zvoykish.restdl.adapters;

import com.zvoykish.restdl.adapters.partials.PartialCodehausJacksonAdapter;
import com.zvoykish.restdl.adapters.partials.PartialJerseyAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 00:59
 */
public class JerseyWithCodehausJacksonAdapter extends BaseJerseyWithJacksonAdapter {
    public JerseyWithCodehausJacksonAdapter() {
        super(new PartialJerseyAdapter(), new PartialCodehausJacksonAdapter());
    }
}
