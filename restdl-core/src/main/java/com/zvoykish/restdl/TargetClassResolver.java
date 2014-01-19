package com.zvoykish.restdl;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/19/14
 * Time: 01:07
 */
public interface TargetClassResolver {
    Class<?> resolveTargetClass(Class<?> clazz);
}
