package org.elementHandler;

import org.context.IContext;
import org.element.TextElement;

import java.util.Objects;

public class TextElementHandler implements IElementHandler<TextElement>{

    @Override
    public TextElement getElementValue(IContext iContext,String key) {
        TextElement textElement = null;
        String value = (String) iContext.getElementTemplate().get(key);
        if(Objects.isNull(value)){
            textElement.setTagName(key);
            textElement.setValue("NAN");
        }else{
            textElement.setTagName(key);
            textElement.setValue(value);
        }
        return textElement;
    }
}
