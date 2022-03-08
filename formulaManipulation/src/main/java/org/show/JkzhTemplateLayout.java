package org.show;

import java.util.HashMap;

public class JkzhTemplateLayout implements ITemplateLayout{
    public HashMap<String,Object> templateLayoutMap = null;
    public String[] templateLayoutChar = null;

    public JkzhTemplateLayout() {
        this.templateLayoutChar = new String[]{"地面堆载", "土层参数计算依据表","土压力系数表",
                "主动土压力合力","土压力零点", "支撑的轴线","主动土压力","被动土压力"};
        this.templateLayoutMap = new HashMap<String, Object>(32);
    }
}
