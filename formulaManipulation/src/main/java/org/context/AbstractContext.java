package org.context;

import lombok.Data;
import org.element.BaseElement;

import java.util.HashMap;
import java.util.List;

@Data
public abstract class AbstractContext {
    //模板元素
    private List<HashMap<String, BaseElement>> elementTemplates;
    //计算过程的中间结果保存
    private List<HashMap<String,String>> temporaryValues;

    //当前第几工况
    private Integer calTimes;
}
