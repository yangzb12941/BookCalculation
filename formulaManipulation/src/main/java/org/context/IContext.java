package org.context;

import lombok.Data;

import java.util.HashMap;

@Data
public abstract class IContext {
    //模板元素
    private HashMap<String,Object> elementTemplate;
    //计算过程的中间结果保存
    private HashMap<String,String> temporaryValue;
}
