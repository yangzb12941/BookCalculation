package org.context;

public class FactoryContext {
    private FactoryContext(){}
    public static AbstractContext getContext(Class < ? extends  AbstractContext> clzss){
        if(clzss.isAssignableFrom(JkzhContext.class)){
            return new JkzhContext();
        }
        return null;
    }
}
