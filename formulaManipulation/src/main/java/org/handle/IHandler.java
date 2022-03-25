package org.handle;

public interface IHandler<T> {
    public String execute(String fromula);
    public IHandler setParams(T t);
}
