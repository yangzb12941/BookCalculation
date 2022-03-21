package org.elementHandler;

import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.context.AbstractContext;

public class ElementHandlerUtils {

    public static Object getElementValue(AbstractContext abstractContext, MetaTemplate metaTemplate){
        Object value = null;
        if(metaTemplate instanceof RunTemplate){
            String source = ((RunTemplate) metaTemplate).getSource();
            if(source.indexOf("#")>=0){
                TableElementHandler tableElementHandler = TableElementHandler.getInstance();
                value = tableElementHandler.getElementValue(abstractContext, metaTemplate);
            }else{
                StringElementHandler stringElementHandler = StringElementHandler.getInstance();
                value = stringElementHandler.getElementValue(abstractContext, metaTemplate);
            }
        }else if (metaTemplate instanceof IterableTemplate){
            JkzhBlockElementHandler jkzhBlockElementHandler = JkzhBlockElementHandler.getInstance();
            value = jkzhBlockElementHandler.getElementValue(abstractContext, metaTemplate);
        }
        return value;
    }
}
