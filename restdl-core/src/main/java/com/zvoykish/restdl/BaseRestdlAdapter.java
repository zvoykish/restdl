package com.zvoykish.restdl;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/19/14
 * Time: 01:56
 */
public abstract class BaseRestdlAdapter implements RestdlAdapter {
    private final String basePackage = System.getProperty("restdl.base.package");

    @Override
    public String getBasePackage() {
        return basePackage;
    }
}
