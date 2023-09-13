package org.handleParams;

import org.calParam.JkzhBasicParam;
import org.config.JkzhConfigEnum;
import org.enums.CalculateSectionEnum;

public class FirstFloorHandlerParam extends AbstractHandleParams {
    private int curFloor;//当前计算土层
    private int beginFloor;//首层土层
    private JkzhConfigEnum  jkzhConfigEnum;//首层土层
    private JkzhBasicParam jkzhBasicParam;

    public FirstFloorHandlerParam(
            JkzhBasicParam jkzhBasicParam,
            int curFloor,
            int beginFloor,
            JkzhConfigEnum  jkzhConfigEnum) {
        this.jkzhBasicParam = jkzhBasicParam;
        this.curFloor = curFloor;
        this.beginFloor = beginFloor;
        this.jkzhConfigEnum = jkzhConfigEnum;
    }

    public Boolean getValue() {
        if(jkzhConfigEnum == JkzhConfigEnum.主动土压力){
            if(curFloor > beginFloor || this.jkzhBasicParam.getSurcharge().compareTo(0.0)>0){
                return Boolean.TRUE;
            }
        }else if(jkzhConfigEnum == JkzhConfigEnum.被动土压力){
            if(curFloor > beginFloor){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
