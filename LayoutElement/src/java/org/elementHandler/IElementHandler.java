package org.elementHandler;

import org.context.IContext;

/**
 * 在上下文中解析出各个模板元素
 * @param <T>
 */
public interface IElementHandler<T> {
    T getElementValue(IContext iContext,String key);
}
