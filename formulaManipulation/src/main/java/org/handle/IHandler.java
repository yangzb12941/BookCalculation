package org.handle;

public interface IHandler<T> {
    public String execute(String fromula);
    public void setParams(T t);
}
