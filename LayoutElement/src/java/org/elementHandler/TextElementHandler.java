package org.elementHandler;

import org.context.IContext;
import org.element.TextElement;

import java.util.Objects;

public class TextElementHandler implements IElementHandler<TextElement>{

    @Override
    public TextElement getElementValue(IContext iContext,String key) {
        String value = (String) iContext.getElementTemplate().get(key);
        if(Objects.isNull(value)){
            return new TextElement(key,"NAN");
        }else{
            return new TextElement(key,value);
        }
    }
}
