package com.zvoykish.restdl.objects;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 00:23
 */
public abstract class BaseTypedObject implements TypedObject {
    private static final AtomicLong counter = new AtomicLong(0);

    private long id;

    protected BaseTypedObject() {
        this.id = counter.getAndIncrement();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public ReferencedTypedObject toReference() {
        return new ReferencedTypedObject(id);
    }
}
