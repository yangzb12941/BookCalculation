package org.table;


/**
 * 基坑支护设计排桩法基本土质参数表
 */
public class SoilQualityTable implements ITable {
    //土层参数计算依据表
    // 层号    土层名称    土层厚度    土的重度    内聚力    内摩擦角(°)
    //土层参数计算依据表头

    //土压力系数头
    private String[][] table = {
            {"岩土层分布（从上至下）及分布特征序号", "土层名称","厚度(m)\nh","重度(kN/m3)\nγ","内聚力(kPa)\nc","内摩擦角(°)\nΨ","计算方式"},
            {"1", "人工填土","1.2","18.0","5.7","13.5","水土合算"},
            {"2", "淤泥质粉质黏土","5","17.8","8.2","9.6","水土合算"},
            {"3", "粉质黏土","3.8","20.0","14","16.2","水土分算"},
            {"4", "黏性土","7.4","20.5","22","20.8","水土合算"},
    };

    @Override
    public String[][] getTable() {
        return this.table;
    }

    public void setTable(String[][] table){
        this.table = table;
    }
}
