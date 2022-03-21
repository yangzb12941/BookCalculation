package org.elementHandler;

import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.context.AbstractContext;
import org.element.BaseElement;


public class StringElementHandler implements IElementHandler<String>{

    private static volatile StringElementHandler instance;

    public static StringElementHandler getInstance(){
        if(instance == null){
            synchronized (instance){
                if (instance == null) {
                    instance = new StringElementHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public String getElementValue(AbstractContext abstractContext, MetaTemplate metaTemplate) {
        RunTemplate runTemplate = (RunTemplate) metaTemplate;
        BaseElement baseElement = abstractContext.getElementTemplate().get(runTemplate.getTagName());
        return (String)baseElement.getValue();
    }
}
