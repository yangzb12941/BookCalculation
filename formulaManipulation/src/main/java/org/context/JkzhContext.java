package org.context;

import lombok.Data;
import org.table.JkzhBasicParam;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

/**
 * 基坑支护计算上下文对象
 */
@Data
public class JkzhContext extends AbstractContext{
    //基坑支护计算 基本参数
    private JkzhBasicParam jkzhBasicParam;

    //土层参数计算依据表
    private SoilQualityTable soilQualityTable;

    //基坑支护 土压力系数表
    private SoilPressureTable soilPressureTable;
}
