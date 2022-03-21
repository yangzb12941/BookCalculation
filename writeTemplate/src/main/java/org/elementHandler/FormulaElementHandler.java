package org.elementHandler;

import org.context.AbstractContext;
import org.element.FormulaElement;

public class FormulaElementHandler implements IElementHandler<FormulaElement>{

    private static volatile FormulaElementHandler instance;

    public static FormulaElementHandler getInstance(){
        if(instance == null){
            synchronized (instance){
                if (instance == null) {
                    instance = new FormulaElementHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public FormulaElement getElementValue(AbstractContext AbstractContext, String key) {
        FormulaElement value = (FormulaElement)AbstractContext.getElementTemplate().get(key);
        return value;
    }
}
