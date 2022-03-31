package org.handleParams;

import org.config.JkzhConfigEnum;

public class StrutForceHandlerParam extends AbstractHandleParams {
    private int curTimes;//当前计算土层

    public StrutForceHandlerParam(
            int curTimes) {
        this.curTimes = curTimes;
    }

    public Boolean getValue() {
        if(curTimes > 1){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
