package org.table;


/**
 * 基坑支护设计排桩法基本土质参数表
 */
public class SoilQualityTable implements ITable {
    //土层参数计算依据表
    // 层号    土层名称    土层厚度    土的重度    内聚力    内摩擦角(°)
    private String[][] table = {
            {"1", "人工填土","1.2","18.0","5.7","13.5"},
            {"2", "淤泥质粉质黏土","5","17.8","8.2","9.6"},
            {"3", "粉质黏土","3.8","20.0","14","16.2"},
            {"4", "黏性土","7.4","20.5","22","20.8"},
    };

    @Override
    public String[][] getTable() {
        return this.table;
    }
}
