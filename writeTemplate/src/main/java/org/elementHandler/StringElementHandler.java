package org.elementHandler;

import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.context.AbstractContext;
import org.element.BaseElement;

import java.util.Objects;


public class StringElementHandler implements IElementHandler<String>{

    private static volatile StringElementHandler instance;

    public static StringElementHandler getInstance(){
        if(instance == null){
            synchronized (StringElementHandler.class){
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
        BaseElement baseElement = abstractContext.getElementTemplates().get(abstractContext.getCalTimes()).get(runTemplate.getTagName());
        if(Objects.nonNull(baseElement)){
            return (String)baseElement.getValue();
        }else{
            return "NAN";
        }
    }
}
