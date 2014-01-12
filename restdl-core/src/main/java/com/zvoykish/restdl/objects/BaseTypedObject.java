package com.zvoykish.restdl.objects;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/10/14
 * Time: 00:23
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class BaseTypedObject extends ClassAwareTypedObject {
    private static final AtomicLong counter = new AtomicLong(0);

    private long id;

    protected BaseTypedObject() {
        this.id = counter.getAndIncrement();
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public ReferencedTypedObject toReference() {
        return new ReferencedTypedObject(id);
    }

    @Override
    public String toString() {
        return "{id=" + id + '}';
    }
}
