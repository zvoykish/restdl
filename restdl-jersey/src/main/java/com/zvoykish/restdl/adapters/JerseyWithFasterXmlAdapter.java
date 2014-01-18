package com.zvoykish.restdl.adapters;

import com.zvoykish.restdl.adapters.partials.PartialFasterXmlJacksonAdapter;
import com.zvoykish.restdl.adapters.partials.PartialJerseyAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/17/14
 * Time: 14:12
 */
public class JerseyWithFasterXmlAdapter extends BaseJerseyWithJacksonAdapter {
    public JerseyWithFasterXmlAdapter() {
        super(new PartialJerseyAdapter(), new PartialFasterXmlJacksonAdapter());
    }
}
