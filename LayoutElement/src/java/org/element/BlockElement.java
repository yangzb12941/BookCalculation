package org.element;

import org.apache.commons.collections.CollectionUtils;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class BlockElement extends BaseElement<List<BaseElement>> {
    public BlockElement(String tagName, List<BaseElement> value) {
        super(tagName, value);
    }

    public Map<String,Object> getValues(){
        Map<String,Object> valueMap = new Hashtable<String,Object>(64);
        List<BaseElement> value = super.getValue();
        if(CollectionUtils.isNotEmpty(value)){
            for (BaseElement element : value) {
                valueMap.put(element.getTagName(),element.getValue());
            }
        }
        return valueMap;
    }
}
