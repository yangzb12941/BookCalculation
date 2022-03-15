package org.elementHandler;

import org.context.IContext;
import org.element.FormulaElement;
import org.element.TextElement;

public class FormulaElementHandler implements IElementHandler<FormulaElement>{
    @Override
    public FormulaElement getElementValue(IContext iContext, String key) {
        String value = (String)iContext.getElementTemplate().get(key);
        return new FormulaElement(key,value);
    }
}
