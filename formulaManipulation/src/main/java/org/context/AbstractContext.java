package org.context;

import lombok.Data;
import org.element.BaseElement;

import java.util.HashMap;
import java.util.List;

@Data
public abstract class AbstractContext {

    //模板基础元素
    private HashMap<String, BaseElement> baseTemplates;

    //多工况模板元素
    private List<HashMap<String, BaseElement>> elementTemplates;

    //计算过程的中间结果保存
    private List<HashMap<String,String>> temporaryValues;

    //最大弯矩模板元素
    private List<HashMap<String, BaseElement>> bendingMomentTemplates;

    //最大弯矩计算过程的中间结果保存
    private List<HashMap<String,String>> bendingMomentValues;

    //当前第几工况
    private Integer calTimes;

    //当前第几个支撑
    private Integer tcTimes;
}
