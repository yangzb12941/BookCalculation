package org.context;

import lombok.Data;
import org.element.BaseElement;

import java.util.HashMap;

@Data
public abstract class AbstractContext {
    //模板元素
    private HashMap<String, BaseElement> elementTemplate;
    //计算过程的中间结果保存
    private HashMap<String,String> temporaryValue;
}
