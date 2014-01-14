package com.zvoykish.restdl.generator;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/14/14
 * Time: 01:42
 */
public class HttpClientFactoryInterfaceImpl implements HttpClientFactoryInterface {
    private String className;
    private String contents;

    public HttpClientFactoryInterfaceImpl(String className, String contents) {
        this.className = className;
        this.contents = contents;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getContents() {
        return contents;
    }
}
