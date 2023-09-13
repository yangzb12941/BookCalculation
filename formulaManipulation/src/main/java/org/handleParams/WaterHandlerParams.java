package org.handleParams;

import org.enums.CalculateSectionEnum;
import org.enums.WaterCalEnum;
import org.enums.WaterWhichEnum;
import org.calParam.JkzhBasicParam;
import org.table.SoilQualityTable;

public class WaterHandlerParams extends AbstractHandleParams{
    private int curFloor;

    private WaterWhichEnum waterWhichEnum;
    private SoilQualityTable soilQualityTable;
    private JkzhBasicParam jkzhBasicParam;
    private CalculateSectionEnum calculateSectionEnum;//计算切面

    public WaterHandlerParams(SoilQualityTable soilQualityTable,
                              JkzhBasicParam jkzhBasicParam,
                              WaterWhichEnum waterWhichEnum,
                              CalculateSectionEnum calculateSectionEnum,
                              Integer curFloor) {
        this.soilQualityTable = soilQualityTable;
        this.jkzhBasicParam = jkzhBasicParam;
        this.waterWhichEnum = waterWhichEnum;
        this.calculateSectionEnum = calculateSectionEnum;
        this.curFloor = curFloor;
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

    /**
     * 判断当前土层是否在水位线下面
     * @return
     */
    private Boolean isUnderWater(){
        Double depth = atDepth(this.curFloor);
        //主动侧水位
        if(this.waterWhichEnum.getKey().equals(WaterWhichEnum.主动侧水位.getKey())){
            if(this.curFloor >= this.jkzhBasicParam.getCalResult().getZDWaterLand() && depth.compareTo(this.jkzhBasicParam.getZDWarterDepth())>0){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
        }else{
            //被东侧水位
            if(this.curFloor >= this.jkzhBasicParam.getCalResult().getBDWaterLand() && depth.compareTo(this.jkzhBasicParam.getBDWarterDepth())>0){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
        }
    }

    //计算给定深度，返回深度所在土层
    private Double atDepth(int depth){
        //判断开挖深度在第几层土层
        String[][] table = soilQualityTable.getTable();
        Double sumLands = 0.0;
        if(calculateSectionEnum == CalculateSectionEnum.上顶面){
            for(int floor = 1;floor <this.curFloor; floor++){
                sumLands += Double.valueOf(table[floor][2]);
            }
        }else if(calculateSectionEnum == CalculateSectionEnum.下底面){
            for(int floor = 1;floor <=this.curFloor; floor++){
                sumLands += Double.valueOf(table[floor][2]);
            }
        }
        return sumLands;
    }
}
