package org.elementHandler;

import com.deepoove.poi.template.MetaTemplate;
import org.context.AbstractContext;

/**
 * 在上下文中解析出各个模板元素
 * @param <T>
 */
public interface IElementHandler<T> {
    T getElementValue(AbstractContext abstractContext, MetaTemplate metaTemplate);
}
