package org.elementHandler;

import org.context.AbstractContext;
import org.element.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElementHandlerUtils {

    public static Object getElementValue(AbstractContext abstractContext, String key){
        BaseElement baseElement = abstractContext.getElementTemplate().get(key);
        if(baseElement instanceof TextElement){
            TextElementHandler textElementHandler = TextElementHandler.getInstance();
            return textElementHandler.getElementValue(abstractContext, key).getValue();
        }else if(baseElement instanceof FormulaElement){
            FormulaElementHandler formulaElementHandler = FormulaElementHandler.getInstance();
            return formulaElementHandler.getElementValue(abstractContext, key).getValue();
        }else if(baseElement instanceof BlockElement){
            JkzhBlockElementHandler jkzhBlockElementHandler = JkzhBlockElementHandler.getInstance();
            Object elementValue = jkzhBlockElementHandler.getElementValue(abstractContext, key);
            List<BlockElement> value = (List<BlockElement>)elementValue;
            List<Map<String,Object>> list = new ArrayList<>(value.size());
            for (BlockElement block : value) {
                list.add(block.getValues());
            }
            return list;
        }else if(baseElement instanceof TableElement){
            TableElementHandler tableElementHandler = TableElementHandler.getInstance();
            return tableElementHandler.getElementValue(abstractContext, key);
        }
        return null;
    }
}
