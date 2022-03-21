package org.elementHandler;

import org.context.AbstractContext;
import org.element.TextElement;

public class TextElementHandler implements IElementHandler<TextElement>{

    private static volatile TextElementHandler instance;

    public static TextElementHandler getInstance(){
        if(instance == null){
            synchronized (instance){
                if (instance == null) {
                    instance = new TextElementHandler();
                }
            }
        }
        return instance;
    }

    private TextElementHandler(){};

    @Override
    public TextElement getElementValue(AbstractContext abstractContext,String key) {
        TextElement textElement = (TextElement)abstractContext.getElementTemplate().get(key);
        return textElement;
    }
}
