package org.handle;

import org.context.IContext;

public interface ICreateContextHandle<T> {
    public void createContextHandle(IContext iContext,T t);
}
