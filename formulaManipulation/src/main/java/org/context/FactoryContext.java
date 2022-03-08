package org.context;

public class FactoryContext {
    private FactoryContext(){}
    public static IContext getContext(Class < ? extends  IContext> clzss){
        if(clzss.isAssignableFrom(JkzhContext.class)){
            return new JkzhContext();
        }
        return null;
    }
}
