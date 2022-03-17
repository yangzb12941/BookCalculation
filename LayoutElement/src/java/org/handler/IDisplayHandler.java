package org.handler;

import org.context.IContext;

import java.util.Map;

public interface IDisplayHandler {
    public Map<String, Object> handlerDataMap(IContext iContext);
}
