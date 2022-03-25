package org.handleParams;

import org.table.JkzhBasicParam;

public class FirstFloorHandlerParam extends AbstractHandleParams {
    private int curFloor;
    private JkzhBasicParam jkzhBasicParam;
    public FirstFloorHandlerParam(JkzhBasicParam jkzhBasicParam) {
        this.jkzhBasicParam = jkzhBasicParam;
    }

    public Boolean getValue() {
        if(curFloor >1 && this.jkzhBasicParam.getSurcharge().compareTo(0.0)>0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setCurFloor(int curFloor){
        this.curFloor = curFloor;
    }
}
