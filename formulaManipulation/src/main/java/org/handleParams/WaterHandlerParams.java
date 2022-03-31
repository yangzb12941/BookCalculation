package org.handleParams;

import org.enums.WaterCalEnum;
import org.enums.WaterWhichEnum;
import org.calParam.JkzhBasicParam;
import org.table.SoilQualityTable;

public class WaterHandlerParams extends AbstractHandleParams{
    private int curFloor;

    private WaterWhichEnum waterWhichEnum;
    private SoilQualityTable soilQualityTable;
    private JkzhBasicParam jkzhBasicParam;

    public WaterHandlerParams(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam,WaterWhichEnum waterWhichEnum) {
        this.soilQualityTable = soilQualityTable;
        this.jkzhBasicParam = jkzhBasicParam;
        this.waterWhichEnum = waterWhichEnum;
    }

    public Boolean getValue() {
        Boolean one = this.soilQualityTable.getTable()[curFloor][6].equals(WaterCalEnum.水土分算.getValue());
        if(one){
            Boolean two = isUnderWater();
            if(two){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public void setCurFloor(int curFloor){
        this.curFloor = curFloor;
    }

    /**
     * 判断当前土层是否在水位线下面
     * @return
     */
    private Boolean isUnderWater(){
        //主动侧水位
        if(this.waterWhichEnum.getKey().equals(WaterWhichEnum.主动侧水位.getKey())){
            if(this.curFloor >= this.jkzhBasicParam.getCalResult().getZDWaterLand()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
        }else{
            //被东侧水位
            if(this.curFloor >= this.jkzhBasicParam.getCalResult().getBDWaterLand()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
        }
    }
}
